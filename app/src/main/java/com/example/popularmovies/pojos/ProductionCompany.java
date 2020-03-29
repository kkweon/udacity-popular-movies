package com.example.popularmovies.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ProductionCompany {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("logo_path")
    @Expose
    private String logoPath;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("origin_country")
    @Expose
    private String originCountry;
}
