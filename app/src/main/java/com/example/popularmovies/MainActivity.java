package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popularmovies.pojos.Movie;
import com.example.popularmovies.pojos.movielist.MovieResponse;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MovieDetailAdapter movieDetailAdapter;

    private MenuItem popularMoviesMenuItem;
    private MenuItem highestRatingMoviesMenuItem;
    private RecyclerView recyclerViewMoviePoster;
    private ProgressBar progressBarLoading;
    private MenuItem filterByFavoriteMenuItem;
    private ApplicationService applicationService = ApplicationService.getInstance();

    private List<Disposable> subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscriptions = new ArrayList<>();
        setContentView(R.layout.activity_main);

        MainActivity context = this;

        progressBarLoading = findViewById(R.id.ProgressBar_loading);

        // Store so we can manipulate when updating data.
        movieDetailAdapter =
                new MovieDetailAdapter(
                        m -> {
                            Intent intent = new Intent(context, MovieDetailsActivity.class);
                            intent.putExtra("Movie", m);
                            startActivity(intent);
                        });

        // This contains LiveData<List<Movie>>. This is the single source of truth for movies data
        // in the UI.
        final ApplicationService applicationService = ApplicationService.getInstance();
        subscriptions.add(
                applicationService
                        .getMoviesObservable()
                        .subscribe(
                                movies -> {
                                    movieDetailAdapter.setMovies(movies);
                                    movieDetailAdapter.notifyDataSetChanged();
                                }));

        subscriptions.add(
                applicationService
                        .getFavoriteMovieIdsObservable()
                        .subscribe(ids -> movieDetailAdapter.notifyDataSetChanged()));

        recyclerViewMoviePoster = findViewById(R.id.RecyclerView_moviePoster);
        recyclerViewMoviePoster.setLayoutManager(
                new GridLayoutManager(
                        this,
                        Utility.calculateNoOfColumns(
                                getApplicationContext(),
                                getResources().getDimension(R.dimen.movie_poster_width))));
        recyclerViewMoviePoster.setAdapter(movieDetailAdapter);

        MovieService movieService = MovieManager.getMovieService();

        // Load the movies by popularity.
        subscriptions.add(
                applicationService
                        .getFetchTypeObservable()
                        .subscribe(
                                fetchType -> {
                                    Log.v(TAG, "FetchType is updated");
                                    fillMovieViewModel(
                                            movieService,
                                            applicationService.getCurrentPage(),
                                            fetchType,
                                            true);
                                    toggleMenu(fetchType);
                                }));
        // If the page changes (scroll to the end), it should fetch the next page.
        subscriptions.add(
                applicationService
                        .getCurrentPageObservable()
                        .subscribe(
                                newPage -> {
                                    // The initialization will be handled when we subscribe to
                                    // FetchType.
                                    if (newPage == 1) return;
                                    Log.v(
                                            TAG,
                                            String.format(
                                                    "Fetching next page movie data (newPage => %d)",
                                                    newPage));
                                    fillMovieViewModel(
                                            movieService,
                                            newPage,
                                            applicationService.getFetchType(),
                                            false);
                                }));

        subscriptions.add(
                applicationService
                        .getIsLoadingObservable()
                        .subscribe(
                                isLoading -> {
                                    if (isLoading) {
                                        progressBarLoading.setVisibility(View.VISIBLE);
                                    } else {
                                        progressBarLoading.setVisibility(View.GONE);
                                    }
                                }));

        recyclerViewMoviePoster = findViewById(R.id.RecyclerView_moviePoster);
        recyclerViewMoviePoster.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        // When it's filtering mode, don't fetch new data.
                        if (applicationService.shouldFilterByFavorite()) return;

                        GridLayoutManager layoutManager =
                                (GridLayoutManager) recyclerView.getLayoutManager();

                        if (!applicationService.getIsLoading()
                                && layoutManager != null
                                && layoutManager.findLastCompletelyVisibleItemPosition()
                                        == applicationService.getMovies().size() - 1) {
                            applicationService.setPage(applicationService.getCurrentPage() + 1);
                        }
                    }
                });
    }

    /**
     * Shows the appropriate menu item.
     *
     * <p>For example, if the movies are sorted by popularity, then show the "Sort by rating" option
     * and hide "Sort by popularity" menu item as it's redundant.
     *
     * @param fetchType
     */
    private void toggleMenu(MovieService.FetchType fetchType) {
        if (popularMoviesMenuItem == null || highestRatingMoviesMenuItem == null) {
            return;
        }
        if (fetchType == MovieService.FetchType.POPULAR_MOVIES) {
            setTitle("Popular Movies");
            popularMoviesMenuItem.setVisible(false);
            highestRatingMoviesMenuItem.setVisible(true);
        } else {
            setTitle("Top Rated Movies");
            popularMoviesMenuItem.setVisible(true);
            highestRatingMoviesMenuItem.setVisible(false);
        }
    }

    private void fillMovieViewModel(
            MovieService movieService, int page, MovieService.FetchType fetchType, boolean reset) {

        // If "Filter by favorite mode" don't need to fetch movies.
        if (applicationService.shouldFilterByFavorite()) {
            return;
        }

        Call<MovieResponse> moviesCall = null;
        applicationService.setLoading(true);
        if (fetchType == MovieService.FetchType.POPULAR_MOVIES) {

            moviesCall =
                    movieService.getPopularMovies(
                            BuildConfig.TMDB_API_KEY,
                            page,
                            getString(R.string.language),
                            getString(R.string.country));
        } else {
            moviesCall =
                    movieService.getTopRatedMovies(
                            BuildConfig.TMDB_API_KEY,
                            page,
                            getString(R.string.language),
                            getString(R.string.country));
        }

        moviesCall.enqueue(
                new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(
                            Call<MovieResponse> call, Response<MovieResponse> response) {
                        MovieResponse movieResponse = response.body();

                        ApplicationService applicationService = ApplicationService.getInstance();
                        if (reset) {
                            applicationService.clearMovies();
                        }

                        applicationService.addMovies(
                                movieResponse.getResults().stream()
                                        .map(m -> Movie.fromMovieResponseItem(m))
                                        .collect(Collectors.toList()));

                        applicationService.setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.v(TAG, String.format("Failed to fetch movies data"));
                        applicationService.setLoading(false);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
        popularMoviesMenuItem = menu.findItem(R.id.action_sort_popularity);
        highestRatingMoviesMenuItem = menu.findItem(R.id.action_sort_rating);
        filterByFavoriteMenuItem = menu.findItem(R.id.action_filter_by_favorite);

        subscriptions.add(
                ApplicationService.getInstance()
                        .getFilterByFavoriteObservable()
                        .doOnNext(
                                newState -> {
                                    Log.d(
                                            TAG,
                                            String.format(
                                                    "\n=========================== newState => %s",
                                                    newState.toString()));
                                })
                        .subscribe(
                                newState -> {
                                    filterByFavoriteMenuItem.setChecked(newState);

                                    if (movieDetailAdapter != null) {
                                        movieDetailAdapter.notifyDataSetChanged();
                                    }
                                }));

        toggleMenu(applicationService.getFetchType());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                this.applicationService.setMovieServiceFetchType(
                        MovieService.FetchType.POPULAR_MOVIES);
                return true;
            case R.id.action_sort_rating:
                this.applicationService.setMovieServiceFetchType(
                        MovieService.FetchType.TOP_RATED_MOVIES);
                return true;
            case R.id.action_filter_by_favorite:
                boolean shouldFilterByFavorite = !filterByFavoriteMenuItem.isChecked();
                ApplicationService.getInstance().setFilterByFavorite(shouldFilterByFavorite);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.forEach(s -> s.dispose());
    }
}
