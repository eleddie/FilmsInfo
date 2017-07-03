package com.proyectosyntax.codingchallenge.models

import com.google.gson.annotations.SerializedName
import ninja.sakib.pultusorm.annotations.PrimaryKey

class Movie : BaseFilm() {
    @SerializedName("title")
    var title: String? = ""
    @SerializedName("original_title")
    var originalTitle: String? = ""
    @SerializedName("release_date")
    var releaseDate: String? = ""
    @SerializedName("video")
    var video: Boolean = false
    @SerializedName("adult")
    var adult: Boolean = false

    fun toSQLite(): MovieSQLite {
        val movieSQLite = MovieSQLite()
        movieSQLite.id = id
        movieSQLite.title = if (title != null) title!!.replace("'", "~") else ""
        movieSQLite.voteAverage = if (voteAverage != null) voteAverage!! else 0f
        movieSQLite.adult = adult
        movieSQLite.backdropPath = if (backdropPath != null) backdropPath!!.substring(1) else ""
        movieSQLite.popularity = if (popularity != null) popularity!! else 0f
        movieSQLite.voteCount = if (voteCount != null) voteCount!! else 0
        movieSQLite.genreIds = "[" + genreIds.joinToString("],[") + "]"
        movieSQLite.posterPath = if (posterPath != null) posterPath!!.substring(1) else ""
        movieSQLite.originalLanguage = if (originalLanguage != null) originalLanguage!! else ""
        movieSQLite.overview = if (overview != null) overview!!.replace("'", "~") else ""
        movieSQLite.originalTitle = if (originalTitle != null) originalTitle!!.replace("'", "~") else ""
        movieSQLite.releaseDate = if (releaseDate != null) releaseDate!! else ""
        movieSQLite.video = video
        return movieSQLite
    }


    class MovieSQLite {
        @PrimaryKey
        var id: Long = 0L
        var voteCount: Int = 0
        var voteAverage: Float = 0f
        var popularity: Float = 0f
        var posterPath: String = ""
        var originalLanguage: String = ""
        var genreIds: String = ""
        var backdropPath: String = ""
        var overview: String = ""
        var title: String = ""
        var originalTitle: String = ""
        var releaseDate: String = ""
        var video: Boolean = false
        var adult: Boolean = false


        fun fromSQLite(): Movie {
            val movie = Movie()
            movie.id = id
            movie.title = title.replace("~", "'")
            movie.voteAverage = voteAverage
            movie.adult = adult
            movie.backdropPath = backdropPath
            movie.popularity = popularity
            movie.voteCount = voteCount
            movie.genreIds = ArrayList()
            for (genre in genreIds.replace("[", "").replace("]", "").split(","))
                 movie.genreIds.add(genre.toInt())
            movie.posterPath = posterPath
            movie.originalLanguage = originalLanguage
            movie.overview = overview.replace("~", "'")
            movie.originalTitle = originalTitle.replace("~", "'")
            movie.releaseDate = releaseDate
            movie.video = video
            return movie
        }

        override fun toString(): String {
            return "id: $id\n" +
                    "title: $title\n" +
                    "voteCount: $voteAverage\n" +
                    "overview: $overview\n" +
                    "posterPath: $posterPath\n" +
                    "backdropPath: $backdropPath\n" +
                    "video: $video\n" +
                    "genresId: $genreIds\n" +
                    "popularity: $popularity\n" +
                    "originalLanguage: $originalLanguage\n" +
                    "originalTitle: $originalTitle\n" +
                    "adult: $adult\n" +
                    "releaseDate: $releaseDate\n" +
                    "voteAverage: $voteAverage\n"
        }
    }
}