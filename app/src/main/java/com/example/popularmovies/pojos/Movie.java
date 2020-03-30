package com.example.popularmovies.pojos;

import com.example.popularmovies.pojos.movielist.MovieResponseItem;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Movie implements Serializable {
    private long id;
    private String title;
    private String thumbnail;
    private String synopsis;
    private UserRating userRating;
    private Date releaseDate;

    public static Movie fromMovieResponseItem(MovieResponseItem movieResponseItem) {
        return Movie.builder()
                .id(movieResponseItem.getId())
                .title(movieResponseItem.getTitle())
                .thumbnail("https://image.tmdb.org/t/p/w185" + movieResponseItem.getPosterPath())
                .synopsis(movieResponseItem.getOverview())
                .userRating(new UserRating(movieResponseItem.getVoteAverage()))
                .releaseDate(movieResponseItem.getReleaseDate())
                .build();
    }

    public String getTitle() {
        Calendar c = Calendar.getInstance();
        c.setTime(releaseDate);
        return String.format(Locale.US, "%s (%d)", title, c.get(Calendar.YEAR));
    }
}
