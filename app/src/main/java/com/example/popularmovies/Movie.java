package com.example.popularmovies;

import java.util.Date;
import lombok.Data;

@Data
class Movie {
    private String title;
    private String thumbnail;
    private String synopsis;
    private UserRating userRating;
    private Date releaseDate;
}
