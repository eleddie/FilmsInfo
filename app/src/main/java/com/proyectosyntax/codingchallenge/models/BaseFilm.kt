package com.proyectosyntax.codingchallenge.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


open class BaseFilm : Serializable {
    @SerializedName("id")
    var id: Long = 0
    @SerializedName("vote_count")
    var voteCount: Int? = 0
    @SerializedName("vote_average")
    var voteAverage: Float? = 0f
    @SerializedName("popularity")
    var popularity: Float? = 0f
    @SerializedName("poster_path")
    var posterPath: String? = ""
    @SerializedName("original_language")
    var originalLanguage: String? = ""
    @SerializedName("genre_ids")
    var genreIds: ArrayList<Int> = ArrayList()
    @SerializedName("backdrop_path")
    var backdropPath: String? = ""
    @SerializedName("overview")
    var overview: String? = ""
}
