package com.example.popularmovies.pojos.movielist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class MovieResponseItem {
    @SerializedName("popularity")
    @Expose
    private double popularity;

    @SerializedName("vote_count")
    @Expose
    private long voteCount;

    @SerializedName("video")
    @Expose
    private boolean video;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("adult")
    @Expose
    private boolean adult;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("genre_ids")
    @Expose
    private List<Long> genreIds = null;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private Date releaseDate;
}
