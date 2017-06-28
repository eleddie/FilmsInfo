package com.proyectosyntax.codingchallenge.Models

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class Show(
        @SerializedName("original_name") var originalName: String,
        @SerializedName("id") var id: Long,
        @SerializedName("name") var name: String,
        @SerializedName("vote_count") var voteCount: Int,
        @SerializedName("vote_average") var voteAverage: Float,
        @SerializedName("poster_path") var posterPath: String,
        @SerializedName("first_air_date") var firstAirDate: String,
        @SerializedName("popularity") var popularity: Float,
        @SerializedName("genre_ids") var genreIds: Array<Int>,
        @SerializedName("original_language") var originalLanguage: String,
        @SerializedName("backdrop_path") var backdropPath: String,
        @SerializedName("overview") var overview: String,
        @SerializedName("origin_country") var originCountry: ArrayList<String>
)