package com.example.popularmovies;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This returns a MovieService so retrofit doesn't need to be created every time.
 */
public class MovieServiceFactory {
    private static Retrofit retrofit;

    public static MovieService getMovieService() {
        if (retrofit == null) {
            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(MovieService.BASE_TMDB_HOST)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        }
        return retrofit.create(MovieService.class);
    }
}
