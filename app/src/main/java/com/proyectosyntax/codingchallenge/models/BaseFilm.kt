package com.proyectosyntax.codingchallenge.models

import com.google.gson.annotations.SerializedName

import java.io.Serializable


open class BaseFilm : Serializable {
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("vote_count")
    var voteCount: Int = 0
    @SerializedName("vote_average")
    var voteAverage: Float = 0.toFloat()
    @SerializedName("popularity")
    var popularity: Float = 0.toFloat()
    @SerializedName("poster_path")
    var posterPath: String? = null
    @SerializedName("original_language")
    var originalLanguage: String? = null
    @SerializedName("genre_ids")
    var genreIds: IntArray? = null
    @SerializedName("backdrop_path")
    var backdropPath: String? = null
    @SerializedName("overview")
    var overview: String? = null
}
