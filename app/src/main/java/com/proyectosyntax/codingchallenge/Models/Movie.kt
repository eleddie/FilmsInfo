package com.proyectosyntax.codingchallenge.Models

import com.google.gson.annotations.SerializedName

class Movie : BaseFilm() {
    @SerializedName("title")
    var title: String? = null
    @SerializedName("original_title")
    var originalTitle: String? = null
    @SerializedName("release_date")
    var releaseDate: String? = null
    @SerializedName("video")
    var video: String? = null
    @SerializedName("adult")
    var adult: Boolean = false
}