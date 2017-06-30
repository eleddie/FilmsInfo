package com.proyectosyntax.codingchallenge.models

import com.google.gson.annotations.SerializedName

class Show : BaseFilm() {
    @SerializedName("origin_country")
    lateinit var originCountry: Array<String>
    @SerializedName("original_name")
    lateinit var originalName: String
    @SerializedName("name")
    lateinit var name: String
    @SerializedName("first_air_date")
    lateinit var firstAirDate: String
}