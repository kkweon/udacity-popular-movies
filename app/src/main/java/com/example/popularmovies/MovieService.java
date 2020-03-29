package com.example.popularmovies;

import com.example.popularmovies.pojos.movielist.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    // See https://themoviedb.org for the details.
    static final String BASE_TMDB_HOST = "https://api.themoviedb.org/3";

    static final String POPULAR_MOVIES_PATH = "/movie/popular";
    static final String TOP_RATED_MOVIES_PATH = "/movie/top_rated";

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
}
