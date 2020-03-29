package com.example.popularmovies.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class MovieResponse {
    @SerializedName("page")
    @Expose
    private long page;

    @SerializedName("total_results")
    @Expose
    private long totalResults;

    @SerializedName("total_pages")
    @Expose
    private long totalPages;

    @SerializedName("results")
    @Expose
    private List<MovieResponseItem> results = null;
}
