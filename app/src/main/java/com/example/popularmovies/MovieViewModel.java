package com.example.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.popularmovies.pojos.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        List<Movie> oldList = movies.getValue();
        oldList.add(movie);
        movies.setValue(oldList);
    }

    public void addMovies(List<Movie> newMovies) {
        List<Movie> oldList = movies.getValue();
        oldList.addAll(newMovies);
        movies.setValue(oldList);
    }

    public void clearMovies() {
        movies.setValue(new ArrayList<>());
    }
}
