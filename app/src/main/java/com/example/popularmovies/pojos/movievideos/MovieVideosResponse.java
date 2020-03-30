package com.example.popularmovies.pojos.movievideos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class MovieVideosResponse implements Serializable {
    @SerializedName("id")
    @Expose
    public long id;

    @SerializedName("results")
    @Expose
    public List<MovieVideosResponseItem> results = null;

    private static final long serialVersionUID = 8970168937888635961L;
}
