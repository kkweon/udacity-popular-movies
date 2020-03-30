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

    private MoviePosterAdapter movieDetailAdapter;

    private MenuItem popularMoviesMenuItem;
    private MenuItem highestRatingMoviesMenuItem;
    private RecyclerView recyclerViewMoviePoster;
    private ProgressBar progressBarLoading;
    private MenuItem filterByFavoriteMenuItem;
    final private ApplicationService applicationService = ApplicationService.getInstance();

    private List<Disposable> subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscriptions = new ArrayList<>();
        setContentView(R.layout.activity_main);

        MainActivity context = this;

        // When loading the movies, we show the progress bar.
        progressBarLoading = findViewById(R.id.ProgressBar_loading);
        recyclerViewMoviePoster = findViewById(R.id.RecyclerView_moviePoster);

        // Store so we can manipulate when updating data.
        movieDetailAdapter =
                new MoviePosterAdapter(
                        m -> {
                            Intent intent = new Intent(context, MovieDetailsActivity.class);
                            intent.putExtra("Movie", m);
                            startActivity(intent);
                        });

        // Every time `movies` changes in the ApplicationState, we update the view.
        subscriptions.add(
                applicationService
                        .getMoviesObservable()
                        .subscribe(
                                movies -> {
                                    movieDetailAdapter.setMovies(movies);
                                    movieDetailAdapter.notifyDataSetChanged();
                                }));

        // Every time favorite movie list changes, we update the view.
        subscriptions.add(
                applicationService
                        .getFavoriteMovieIdsObservable()
                        .subscribe(ids -> movieDetailAdapter.notifyDataSetChanged()));


        // We compute the best number of columns on the fly.
        recyclerViewMoviePoster.setLayoutManager(
                new GridLayoutManager(
                        this,
                        Utility.calculateNoOfColumns(
                                getApplicationContext(),
                                getResources().getDimension(R.dimen.movie_poster_width))));
        recyclerViewMoviePoster.setAdapter(movieDetailAdapter);


        // This is the API service that communicates with themoviedb.org.
        MovieService movieService = MovieServiceFactory.getMovieService();

        // The fetch type is the sort type (Sort by movie popularity or movie rating).
        // Whenenver it changes, we update the view.
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

        // When a user scrolls to the bottom of the list, it will fetch a next page.
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

        // Set the progress bar while the data is loading.
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


        // When a scroll reaches the end, it should fetch new data except it's "Favorite only" mode.
        recyclerViewMoviePoster.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        // When it's filtering mode, don't fetch new data.
                        if (applicationService.shouldFilterByFavorite())
                            return;

                        GridLayoutManager layoutManager =
                                (GridLayoutManager) recyclerView.getLayoutManager();

                        if (!applicationService.getIsLoading()
                                && layoutManager != null
                                && layoutManager.findLastCompletelyVisibleItemPosition()
                                == applicationService.getMovies()
                                                     .size() - 1) {
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
                                movieResponse.getResults()
                                             .stream()
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
                                  .subscribe(
                                          newState -> {
                                              filterByFavoriteMenuItem.setChecked(newState);

                                              if (movieDetailAdapter != null) {
                                                  movieDetailAdapter.notifyDataSetChanged();
                                              }
                                          }));

        // Handles such that it doesn't show the both sort options since the current view is already sorted by either rule.
        // Then, it hides the current sort option.
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
                ApplicationService.getInstance()
                                  .setFilterByFavorite(shouldFilterByFavorite);
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
