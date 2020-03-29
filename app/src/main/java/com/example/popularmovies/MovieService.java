package com.example.popularmovies;

import android.net.Uri;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    // See https://themoviedb.org for the details.
    private static final String BASE_TMDB_HOST = "https://api.themoviedb.org/3";

    private static final String POPULAR_MOVIES_PATH = "/movie/popular";
    private static final String TOP_RATED_MOVIES_PATH = "/movie/top_rated";


    List<Movie> getPopularMovies(int page) {
        Uri uri = Uri.parse(BASE_TMDB_HOST)
                     .buildUpon()
                     .path(POPULAR_MOVIES_PATH)
                     .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                     .appendQueryParameter("page", String.valueOf(page))
                     .build();


        return new ArrayList<>();
    }
}
