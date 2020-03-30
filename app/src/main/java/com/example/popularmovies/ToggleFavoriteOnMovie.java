package com.example.popularmovies;

import com.example.popularmovies.pojos.Movie;
import lombok.Data;

@Data
public class ToggleFavoriteOnMovie {
    private final Movie movie;

    public ToggleFavoriteOnMovie(Movie movie) {
        this.movie = movie;
    }
}
