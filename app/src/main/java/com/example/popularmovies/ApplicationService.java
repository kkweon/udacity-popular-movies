package com.example.popularmovies;

import com.example.popularmovies.pojos.Movie;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationService {
    private static ApplicationService instance = null;

    private BehaviorSubject<List<Movie>> movies;
    private BehaviorSubject<Set<Long>> favoriteMovieIds;
    private BehaviorSubject<Boolean> shouldFilterByFavorite;

    private List<Movie> previousMovies = null;

    private ApplicationService() {
        movies = BehaviorSubject.createDefault(new ArrayList<>());
        favoriteMovieIds = BehaviorSubject.createDefault(new HashSet<>());
        shouldFilterByFavorite = BehaviorSubject.createDefault(false);
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
            oldSet.remove(id);
        } else {
            oldSet.add(id);
        }
        favoriteMovieIds.onNext(oldSet);
    }

    public void setFilterByFavorite(boolean nextState) {
        shouldFilterByFavorite.onNext(nextState);

        if (nextState) {
            // filter
            previousMovies = movies.getValue();
            List<Movie> favoriteMovies =
                    movies.getValue().stream()
                            .filter(
                                    m -> {
                                        return favoriteMovieIds.getValue().contains(m.getId());
                                    })
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
}
