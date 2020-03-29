package com.example.popularmovies;

import lombok.Data;

@Data
class MovieItemResponse {
    private String overview;
    private String original_language;
    private String original_title;
    private String video;
    private String title;
    private String[] genre_ids;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private String popularity;
    private String vote_average;
    private String id;
    private String adult;
    private String vote_count;
}
