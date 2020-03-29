package com.example.popularmovies.pojos.moviedetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SpokenLanguage {

    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;

    @SerializedName("name")
    @Expose
    private String name;
}
