package com.example.popularmovies;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieManager {
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
