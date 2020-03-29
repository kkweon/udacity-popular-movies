package com.example.popularmovies;

import java.util.Date;
import lombok.Data;

@Data
class MovieItemResponse {
    private String overview;
    private String original_language;
    private String original_title;
    private boolean video;
    private String title;
    private int[] genre_ids;
    private String poster_path;
    private String backdrop_path;
    private Date release_date;
    private double popularity;
    private double vote_average;
    private int id;
    private boolean adult;
    private int vote_count;
}
