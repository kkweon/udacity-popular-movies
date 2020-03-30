package com.example.popularmovies.pojos.movievideos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class MovieVideosResponseItem implements Serializable {
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("iso_639_1")
    @Expose
    public String iso6391;

    @SerializedName("iso_3166_1")
    @Expose
    public String iso31661;

    @SerializedName("key")
    @Expose
    public String key;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("site")
    @Expose
    public String site;

    @SerializedName("size")
    @Expose
    public long size;

    @SerializedName("type")
    @Expose
    public String type;

    private static final long serialVersionUID = -3944814520088839256L;
}
