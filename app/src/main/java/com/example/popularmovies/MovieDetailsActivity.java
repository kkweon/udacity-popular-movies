package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popularmovies.pojos.Movie;
import com.example.popularmovies.pojos.moviedetails.MovieDetailsResponse;
import com.example.popularmovies.pojos.moviereviews.MovieReviewsResponse;
import com.example.popularmovies.pojos.moviereviews.MovieReviewsResponseItem;
import com.example.popularmovies.pojos.movievideos.MovieVideosResponse;
import com.example.popularmovies.pojos.movievideos.MovieVideosResponseItem;
import com.squareup.picasso.Picasso;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Movie Details Page.
 *
 * <p>This is the second activity where a user can see the detailed information of the movie.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    public static final String YOUTUBE = "YouTube";
    public static final String HTTPS_YOUTUBE_COM_WATCH = "https://youtube.com/watch";
    public static final String YOUTUBE_VIDEO_ID_QUERY_PARAM = "v";
    private Movie movie;
    private ImageView movieDetailsPoster;
    private TextView movieDetailsRating;
    private TextView movieDetailsSynopsis;
    private TextView movieDetailsTitle;
    private MovieRepository movieRepository;
    private TextView textViewRunningTime;
    private TextView movieDetailsYear;

    private RecyclerView movieTrailers;
    private RecyclerView movieDetailsReviews;
    private Button movieDetailsFavoriteButton;

    private CompositeDisposable subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        subscriptions = new CompositeDisposable();

        // Attach XML elements.
        movieDetailsPoster = findViewById(R.id.ImageView_movieDetailsPoster);
        movieDetailsRating = findViewById(R.id.TextView_movieDetailsRating);
        movieDetailsSynopsis = findViewById(R.id.TextView_movieDetailsSynopsis);
        movieDetailsTitle = findViewById(R.id.TextView_movieDetailsTitle);
        movieDetailsReviews = findViewById(R.id.RecyclerView_movieReviews);
        movieTrailers = findViewById(R.id.RecyclerView_movieTrailers);
        movieDetailsYear = findViewById(R.id.TextView_movieDetailsYear);
        movieDetailsFavoriteButton = findViewById(R.id.Button_addToFavorite);

        Intent intent = getIntent();
        if (intent == null) {
            // This activity should be open with an intent.
            finish();
            return;
        }

        if (intent.hasExtra("Movie")) {
            movie = (Movie) intent.getSerializableExtra("Movie");

            subscriptions.add(
                    ApplicationService.getInstance()
                            .getFavoriteMovieIdsObservable()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    favoriteMovieIds -> {
                                        if (favoriteMovieIds.contains(movie.getId())) {
                                            setFavorite(true);
                                        } else {
                                            setFavorite(false);
                                        }
                                    }));

            Picasso.get().load(movie.getThumbnail()).fit().into(movieDetailsPoster);
            movieDetailsRating.setText(movie.getUserRating().toString());
            movieDetailsSynopsis.setText(movie.getSynopsis());
            movieDetailsTitle.setText(movie.getTitle());
            movieDetailsYear.setText(String.valueOf(movie.getReleaseDate().get(Calendar.YEAR)));

            movieRepository = MovieRepositoryFactory.getMovieRepository();

            // Register click listener to the "Add to favorite" button.
            movieDetailsFavoriteButton.setOnClickListener(
                    v -> ApplicationService.getInstance().toggleFavoriteMovieIds(movie.getId()));

            // Get running time
            movieRepository
                    .getMovieDetails(
                            movie.getId(),
                            BuildConfig.TMDB_API_KEY,
                            getResources().getString(R.string.language))
                    .enqueue(
                            new Callback<MovieDetailsResponse>() {
                                @Override
                                public void onResponse(
                                        Call<MovieDetailsResponse> call,
                                        Response<MovieDetailsResponse> response) {
                                    textViewRunningTime =
                                            findViewById(R.id.TextView_detail_runningTime);
                                    textViewRunningTime.setText(
                                            response.body().getRuntime() + " min");
                                }

                                @Override
                                public void onFailure(
                                        Call<MovieDetailsResponse> call, Throwable t) {}
                            });

            // Get trailers for the movies.
            movieRepository
                    .getVideos(
                            movie.getId(),
                            BuildConfig.TMDB_API_KEY,
                            getResources().getString(R.string.language))
                    .enqueue(
                            new Callback<MovieVideosResponse>() {
                                @Override
                                public void onResponse(
                                        Call<MovieVideosResponse> call,
                                        Response<MovieVideosResponse> response) {
                                    MovieVideosResponse videosResponse = response.body();
                                    // TODO(kkweon): Support other types than YouTube in the future.
                                    List<MovieVideosResponseItem> videosResponseItems =
                                            videosResponse.results.stream()
                                                    .filter(v -> v.site.equals(YOUTUBE))
                                                    .collect(Collectors.toList());

                                    initializeTrailers(movieTrailers, videosResponseItems);
                                }

                                @Override
                                public void onFailure(
                                        Call<MovieVideosResponse> call, Throwable t) {}
                            });

            // Get reviews for this movie.
            movieRepository
                    .getReviews(
                            movie.getId(),
                            BuildConfig.TMDB_API_KEY,
                            getResources().getString(R.string.language),
                            1)
                    .enqueue(
                            new Callback<MovieReviewsResponse>() {
                                @Override
                                public void onResponse(
                                        Call<MovieReviewsResponse> call,
                                        Response<MovieReviewsResponse> response) {
                                    List<MovieReviewsResponseItem> reviews =
                                            response.body().getResults();
                                    initializeReviews(
                                            movieDetailsReviews,
                                            new MovieDetailsReviewsAdapter(reviews));
                                }

                                @Override
                                public void onFailure(
                                        Call<MovieReviewsResponse> call, Throwable t) {}
                            });

            setTitle(movie.getTitle());
        }
    }

    private void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            movieDetailsFavoriteButton.setText(R.string.remove_from_favorite);
        } else {
            movieDetailsFavoriteButton.setText(R.string.add_to_favorite);
        }
    }

    private void initializeTrailers(
            RecyclerView movieTrailers, List<MovieVideosResponseItem> videosResponseItems) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        movieTrailers.setLayoutManager(layoutManager);
        movieTrailers.setHasFixedSize(true);
        movieTrailers.setAdapter(new MovieTrailerAdapter(videosResponseItems, this::playVideo));
    }

    private void initializeReviews(
            RecyclerView movieDetailsReviews,
            MovieDetailsReviewsAdapter movieDetailsReviewsAdapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        movieDetailsReviews.setLayoutManager(layoutManager);
        movieDetailsReviews.setHasFixedSize(true);
        movieDetailsReviews.setAdapter(movieDetailsReviewsAdapter);
    }

    private void playVideo(MovieVideosResponseItem video) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(
                Uri.parse(HTTPS_YOUTUBE_COM_WATCH)
                        .buildUpon()
                        .appendQueryParameter(YOUTUBE_VIDEO_ID_QUERY_PARAM, video.key)
                        .build());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.dispose();
    }
}
