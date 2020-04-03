package com.example.popularmovies;

import android.content.Context;
import android.util.Log;
import com.example.popularmovies.persistence.FavoriteMovie;
import com.example.popularmovies.persistence.FavoriteMovieRepository;
import com.example.popularmovies.pojos.Movie;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Global application state is managed here using the reactive pattern. This is the single source of
 * truth.
 */
public class ApplicationService {
    private static final String TAG = ApplicationService.class.getSimpleName();
    private static ApplicationService instance = null;

    private BehaviorSubject<List<Movie>> movies;
    private BehaviorSubject<Set<Long>> favoriteMovieIds;
    private BehaviorSubject<Boolean> shouldFilterByFavorite;

    private List<Movie> previousMovies = null;

    private BehaviorSubject<MovieRepository.FetchType> fetchType;
    private BehaviorSubject<Integer> currentPage;
    private BehaviorSubject<Boolean> isLoading;
    private static FavoriteMovieRepository favoriteMovieRepository;

    private ApplicationService() {
        movies = BehaviorSubject.createDefault(new ArrayList<>());
        favoriteMovieIds = BehaviorSubject.createDefault(new HashSet<>());
        shouldFilterByFavorite = BehaviorSubject.createDefault(false);
        fetchType = BehaviorSubject.createDefault(MovieRepository.FetchType.POPULAR_MOVIES);
        currentPage = BehaviorSubject.createDefault(1);
        isLoading = BehaviorSubject.createDefault(false);

        // Load Favorite Movies when initializing.
        favoriteMovieRepository
                .getFavoriteMovies()
                .map(movies -> movies.stream().map(FavoriteMovie::getId))
                .subscribeOn(Schedulers.io())
                .subscribe(movies -> favoriteMovieIds.onNext(movies.collect(Collectors.toSet())));
    }

    // Initialize the database. Should be called only once.
    public static void initDatabase(Context context) {
        favoriteMovieRepository = new FavoriteMovieRepository(context);
    }

    public void clearMovies() {
        movies.onNext(new ArrayList<>());
    }

    public Observable<List<Movie>> getMoviesObservable() {
        return movies;
    }

    public List<Movie> getMovies() {
        return movies.getValue();
    }

    public static ApplicationService getInstance() {
        if (instance == null) {
            instance = new ApplicationService();
        }
        return instance;
    }

    public Observable<Set<Long>> getFavoriteMovieIdsObservable() {
        return favoriteMovieIds;
    }

    public Set<Long> getFavoriteMovieIds() {
        return favoriteMovieIds.getValue();
    }

    public void addMovies(List<Movie> additionalMovies) {
        List<Movie> oldMovies = movies.getValue();
        oldMovies.addAll(additionalMovies);
        movies.onNext(oldMovies);
    }

    public void toggleFavoriteMovieIds(long id) {
        Set<Long> oldSet = favoriteMovieIds.getValue();
        if (oldSet.contains(id)) {
            favoriteMovieRepository
                    .deleteFavoriteMovie(new FavoriteMovie(id))
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            i -> {
                                oldSet.remove(id);
                                favoriteMovieIds.onNext(oldSet);

                                if (shouldFilterByFavorite()) {
                                    // remove from the current movies.
                                    movies.onNext(
                                            movies.getValue().stream()
                                                    .filter(m -> m.getId() != id)
                                                    .collect(Collectors.toList()));
                                }
                            },
                            i ->
                                    Log.v(
                                            TAG,
                                            String.format(
                                                    "Failed to delete a favorite movie(id = %s)",
                                                    id)));

        } else {
            favoriteMovieRepository
                    .insertFavoriteMovie(new FavoriteMovie(id))
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            i -> {
                                oldSet.add(id);
                                favoriteMovieIds.onNext(oldSet);
                            },
                            i ->
                                    Log.v(
                                            TAG,
                                            String.format(
                                                    "Failed to insert a favorite movie(id = %s)",
                                                    id)));
        }
    }

    public void setFilterByFavorite(boolean nextState) {
        shouldFilterByFavorite.onNext(nextState);

        if (nextState) {
            // filter
            previousMovies = movies.getValue();
            List<Movie> favoriteMovies =
                    movies.getValue().stream()
                            .filter(m -> favoriteMovieIds.getValue().contains(m.getId()))
                            .collect(Collectors.toList());
            movies.onNext(favoriteMovies);
        } else {
            if (previousMovies != null) {
                movies.onNext(previousMovies);
                previousMovies = null;
            }
        }
    }

    public Observable<Boolean> getFilterByFavoriteObservable() {
        return shouldFilterByFavorite;
    }

    public boolean shouldFilterByFavorite() {
        return shouldFilterByFavorite.getValue();
    }

    public Observable<MovieRepository.FetchType> getFetchTypeObservable() {
        return fetchType;
    }

    public Observable<Integer> getCurrentPageObservable() {
        return currentPage;
    }

    public int getCurrentPage() {
        return currentPage.getValue();
    }

    public MovieRepository.FetchType getFetchType() {
        return fetchType.getValue();
    }

    public Observable<Boolean> getIsLoadingObservable() {
        return isLoading;
    }

    public boolean getIsLoading() {
        return isLoading.getValue();
    }

    public void setPage(int newPage) {
        currentPage.onNext(newPage);
    }

    public void setLoading(boolean nextIsLoading) {
        isLoading.onNext(nextIsLoading);
    }

    public void setMovieServiceFetchType(MovieRepository.FetchType nextFetchType) {
        fetchType.onNext(nextFetchType);
    }
}
