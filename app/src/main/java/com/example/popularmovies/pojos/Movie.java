package com.example.popularmovies.pojos;

import com.example.popularmovies.pojos.movielist.MovieResponseItem;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import lombok.Builder;
import lombok.Data;

/** This is the actual view model used in the UI. Select only interested fields to save memory. */
@Data
@Builder
public class Movie implements Serializable {
    private long id;
    private String title;
    private String thumbnail;
    private String synopsis;
    private UserRating userRating;
    private Calendar releaseDate;

    public static Movie fromMovieResponseItem(MovieResponseItem movieResponseItem) {
        Calendar releaseDate = Calendar.getInstance();
        releaseDate.setTime(movieResponseItem.getReleaseDate());
        return Movie.builder()
                .id(movieResponseItem.getId())
                .title(movieResponseItem.getTitle())
                .thumbnail("https://image.tmdb.org/t/p/w185" + movieResponseItem.getPosterPath())
                .synopsis(movieResponseItem.getOverview())
                .userRating(new UserRating(movieResponseItem.getVoteAverage()))
                .releaseDate(releaseDate)
                .build();
    }

    public String getTitle() {
        return String.format(Locale.US, "%s (%d)", title, releaseDate.get(Calendar.YEAR));
    }
}
