package com.example.popularmovies;

import lombok.Data;

@Data
public class MovieResponse {
    private String page;
    private String total_pages;
    private MovieItemResponse[] results;
    private String total_results;
}
