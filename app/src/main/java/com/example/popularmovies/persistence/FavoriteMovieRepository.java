package com.example.popularmovies.persistence;

import android.content.Context;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class FavoriteMovieRepository {
    private FavoriteMovieDao favoriteMovieDao;
    private final FavoriteMovieDatabase database;

    public FavoriteMovieRepository(Context context) {
        database = FavoriteMovieDatabase.getDatabase(context);
        favoriteMovieDao = database.movieDao();
    }

    public Observable<HashSet<FavoriteMovie>> getFavoriteMovies() {

        return Observable.fromFuture(
                        CompletableFuture.supplyAsync(() -> favoriteMovieDao.getMovies()))
                .map(HashSet::new);
    }

    public @NonNull Observable<Long> insertFavoriteMovie(FavoriteMovie movie) {
        return Observable.fromFuture(
                CompletableFuture.supplyAsync(() -> favoriteMovieDao.insert(movie)));
    }

    public @NonNull Observable<Integer> deleteFavoriteMovie(FavoriteMovie movie) {
        return Observable.fromFuture(
                CompletableFuture.supplyAsync(() -> favoriteMovieDao.delete(movie)));
    }
}
