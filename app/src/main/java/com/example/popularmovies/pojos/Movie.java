package com.example.popularmovies.pojos;

import java.util.Date;
import lombok.Data;

@Data
public class Movie {
    private String title;
    private String thumbnail;
    private String synopsis;
    private UserRating userRating;
    private Date releaseDate;
}
