package com.example.popularmovies;

import java.util.List;
import lombok.Data;

@Data
public class MovieResponse {
    private int page;
    private int total_pages;
    private List<MovieItemResponse> results;
    private int total_results;
}
