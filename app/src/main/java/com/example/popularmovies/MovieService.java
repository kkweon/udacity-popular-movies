package com.example.popularmovies;

import com.example.popularmovies.pojos.movielist.MovieResponse;
import com.example.popularmovies.pojos.moviereviews.MovieReviewsResponse;
import com.example.popularmovies.pojos.movievideos.MovieVideosResponse;
import java.io.Serializable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService extends Serializable {
    // See https://themoviedb.org for the details.
    String BASE_TMDB_HOST = "https://api.themoviedb.org/3/";

    enum FetchType {
        POPULAR_MOVIES,
        TOP_RATED_MOVIES
    }

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language,
            @Query("region") String region);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language,
            @Query("region") String region);

    @GET("movie/{movie_id}/videos")
    Call<MovieVideosResponse> getVideos(
            @Path("movie_id") long movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewsResponse> getReviews(
            @Path("movie_id") long movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);
}
