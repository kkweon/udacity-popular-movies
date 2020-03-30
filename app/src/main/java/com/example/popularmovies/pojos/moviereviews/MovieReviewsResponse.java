package com.example.popularmovies.pojos.moviereviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class MovieReviewsResponse implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("page")
    @Expose
    private long page;

    @SerializedName("results")
    @Expose
    private List<MovieReviewsResponseItem> results = null;

    @SerializedName("total_pages")
    @Expose
    private long totalPages;

    @SerializedName("total_results")
    @Expose
    private long totalResults;

    private static final long serialVersionUID = 5344539573770709617L;
}
