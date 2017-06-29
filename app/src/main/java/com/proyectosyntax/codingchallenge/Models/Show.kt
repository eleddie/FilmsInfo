package com.proyectosyntax.codingchallenge.Models

import com.google.gson.annotations.SerializedName

class Show : BaseFilm() {
    @SerializedName("origin_country")
    var originCountry: Array<String>? = null
    @SerializedName("original_name")
    var originalName: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("first_air_date")
    var firstAirDate: String? = null
}