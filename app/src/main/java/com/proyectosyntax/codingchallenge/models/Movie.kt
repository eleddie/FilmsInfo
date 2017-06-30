package com.proyectosyntax.codingchallenge.models

import com.google.gson.annotations.SerializedName

class Movie : BaseFilm() {
    @SerializedName("title")
    lateinit var title: String
    @SerializedName("original_title")
    lateinit var originalTitle: String
    @SerializedName("release_date")
    lateinit var releaseDate: String
    @SerializedName("video")
    lateinit var video: String
    @SerializedName("adult")
    var adult: Boolean = false
}