package com.example.popularmovies;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** This returns a MovieService so retrofit doesn't need to be created every time. */
class MovieRepositoryFactory {
    private static Retrofit retrofit;

    static MovieRepository getMovieRepository() {
        if (retrofit == null) {
            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(MovieRepository.BASE_TMDB_HOST)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        }
        return retrofit.create(MovieRepository.class);
    }
}
