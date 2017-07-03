package com.proyectosyntax.codingchallenge.models

import com.google.gson.annotations.SerializedName
import ninja.sakib.pultusorm.annotations.PrimaryKey

class Show : BaseFilm() {
    @SerializedName("origin_country")
    var originCountry: ArrayList<String>? = arrayListOf()
    @SerializedName("original_name")
    var originalName: String? = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("first_air_date")
    var firstAirDate: String? = ""


    fun toSQLite(): ShowSQLite {
        val movieSQLite = ShowSQLite()
        movieSQLite.id = id
        movieSQLite.name = name.replace("'", "~")
        movieSQLite.voteAverage = if (voteAverage != null) voteAverage!! else 0f
        movieSQLite.backdropPath = if (backdropPath != null) backdropPath!!.substring(1) else ""
        movieSQLite.popularity = if (popularity != null) popularity!! else 0f
        movieSQLite.voteCount = if (voteCount != null) voteCount!! else 0
        movieSQLite.genreIds = "[" + genreIds.joinToString("],[") + "]"
        movieSQLite.posterPath = if (posterPath != null) posterPath!!.substring(1) else ""
        movieSQLite.originalLanguage = if (originalLanguage != null) originalLanguage!! else ""
        movieSQLite.overview = if (overview != null) overview!!.replace("'", "~") else ""
        movieSQLite.originalName = if (originalName != null) originalName!!.replace("'", "~") else ""
        movieSQLite.firstAirDate = if (firstAirDate != null) firstAirDate!! else ""
        return movieSQLite
    }


    class Seasons {
        @SerializedName("id")
        var id: Int = 0
        @SerializedName("air_date")
        var airDate: String = ""
        @SerializedName("episode_count")
        var episodeCount: Int = 0
        @SerializedName("poster_path")
        var posterPath: String = ""
        @SerializedName("season_number")
        var seasonNumber: Int = 0
    }

    class ShowSQLite {
        @PrimaryKey
        var id: Long = 0
        var voteCount: Int = 0
        var voteAverage: Float = 0f
        var popularity: Float = 0f
        var posterPath: String = ""
        var originalLanguage: String = ""
        var genreIds: String = ""
        var backdropPath: String = ""
        var overview: String = ""
        var name: String = ""
        var originalName: String = ""
        var firstAirDate: String = ""

        fun fromSQLite(): Show {
            val show = Show()
            show.id = id
            show.name = name.replace("~", "'")
            show.voteAverage = voteAverage
            show.backdropPath = backdropPath
            show.popularity = popularity
            show.voteCount = voteCount
            show.genreIds = ArrayList()
            for (genre in genreIds.replace("[", "").replace("]", "").split(","))
                show.genreIds.add(genre.toInt())
            show.posterPath = posterPath
            show.originalLanguage = originalLanguage
            show.overview = overview.replace("~", "'")
            show.originalName = originalName.replace("~", "'")
            show.firstAirDate = firstAirDate
            return show
        }

        override fun toString(): String {
            return "id: $id\n" +
                    "name: $name\n" +
                    "voteCount: $voteCount\n" +
                    "overview: $overview\n" +
                    "posterPath: $posterPath\n" +
                    "backdropPath: $backdropPath\n" +
                    "genresId: $genreIds\n" +
                    "popularity: $popularity\n" +
                    "originalLanguage: $originalLanguage\n" +
                    "originalName: $originalName\n" +
                    "firstDateAir: $firstAirDate\n" +
                    "voteAverage: $voteAverage\n"
        }
    }

}