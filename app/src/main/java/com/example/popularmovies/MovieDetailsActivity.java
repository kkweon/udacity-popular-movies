package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popularmovies.pojos.Movie;
import com.example.popularmovies.pojos.moviereviews.MovieReviewsResponse;
import com.example.popularmovies.pojos.moviereviews.MovieReviewsResponseItem;
import com.example.popularmovies.pojos.movievideos.MovieVideosResponse;
import com.example.popularmovies.pojos.movievideos.MovieVideosResponseItem;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String YOUTUBE = "YouTube";
    public static final String HTTPS_YOUTUBE_COM_WATCH = "https://youtube.com/watch";
    public static final String YOUTUBE_VIDEO_ID_QUERY_PARAM = "v";
    private ImageView movieDetailsPoster;
    private TextView movieDetailsRating;
    private TextView movieDetailsSynposis;
    private TextView movieDetailsTitle;
    private MovieService movieService;
    private ImageButton movieDetailsPlayButton;
    private MovieVideosResponseItem movieVideosResponseItem;

    private RecyclerView movieDetailsReviews;
    private MovieDetailsReviewsAdapter movieDetailsReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Attach XML elements.
        movieDetailsPoster = findViewById(R.id.ImageView_movieDetailsPoster);
        movieDetailsRating = findViewById(R.id.TextView_movieDetailsRating);
        movieDetailsSynposis = findViewById(R.id.TextView_movieDetailsSynopsis);
        movieDetailsTitle = findViewById(R.id.TextView_movieDetailsTitle);
        movieDetailsPlayButton = findViewById(R.id.ImageButton_movieDetailsPlayButton);
        movieDetailsPlayButton.setOnClickListener(v -> playVideo(movieVideosResponseItem));
        movieDetailsReviews = findViewById(R.id.RecyclerView_movieReviews);
        initializeReviews(movieDetailsReviews, movieDetailsReviewsAdapter);

        Intent intent = getIntent();
        if (intent == null) {
            // This activity should be open with an intent.
            finish();
            return;
        }

        if (intent.hasExtra("Movie")) {
            Movie movie = (Movie) intent.getSerializableExtra("Movie");

            Picasso.get().load(movie.getThumbnail()).fit().into(movieDetailsPoster);
            movieDetailsRating.setText(movie.getUserRating().toString());
            movieDetailsSynposis.setText(movie.getSynopsis());
            movieDetailsTitle.setText(movie.getTitle());

            movieService = MovieManager.getMovieService();
            Call<MovieVideosResponse> movieVideosResponse =
                    movieService.getVideos(
                            movie.getId(),
                            BuildConfig.TMDB_API_KEY,
                            getResources().getString(R.string.language));
            movieVideosResponse.enqueue(
                    new Callback<MovieVideosResponse>() {
                        @Override
                        public void onResponse(
                                Call<MovieVideosResponse> call,
                                Response<MovieVideosResponse> response) {
                            MovieVideosResponse videosResponse = response.body();
                            // TODO(kkweon): Suppose other types than YouTube in the future.
                            List<MovieVideosResponseItem> videosResponseItems =
                                    videosResponse.results.stream()
                                            .filter(v -> v.site.equals(YOUTUBE))
                                            .collect(Collectors.toList());
                            if (!videosResponseItems.isEmpty()) {
                                movieVideosResponseItem = videosResponseItems.get(0);
                            } else {
                                // Since there's no video attached to this video, hide the play
                                // button.
                                movieDetailsPlayButton.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieVideosResponse> call, Throwable t) {}
                    });

            movieService
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
}
