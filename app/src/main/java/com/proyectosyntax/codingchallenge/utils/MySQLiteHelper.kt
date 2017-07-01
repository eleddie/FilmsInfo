package com.proyectosyntax.codingchallenge.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.models.Show


class MySQLiteHelper(context: Context) : SQLiteOpenHelper(context, MySQLiteHelper.DATABASE_NAME, null, MySQLiteHelper.DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_MOVIES)
        database.execSQL(CREATE_SHOWS)
        database.execSQL(CREATE_SEASONS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(MySQLiteHelper::class.java.name,
                "Upgrading database from version $oldVersion to $newVersion, which will destroy all old data")
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES)
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_SHOWS)
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_SEASONS)
        onCreate(db)
    }

    companion object {
        val COLUMN_ID = "id"
        val TABLE_MOVIES = "movies"
        val TABLE_SHOWS = "shows"
        val TABLE_SEASONS = "seasons"
        val COLUMN_TITLE = "title"
        val COLUMN_NAME = "name"
        val COLUMN_POPULARITY = "popularity"
        val COLUMN_OVERVIEW = "overview"
        val COLUMN_POSTER_PATH = "poster_path"
        val COLUMN_BACKDROP_PATH = "backdrop_path"
        val COLUMN_GENRES = "genres"
        val COLUMN_RELEASE_DATE = "release_date"
        val COLUMN_SEASONS_ID = "seasons_id"
        val COLUMN_FIRST_AIR_DATE = "first_air_date"
        val COLUMN_AIR_DATE = "air_date"
        val COLUMN_SEASON_NUMBER = "season_number"
        val COLUMN_EPISODE_COUNT = "episode_count"

        val DATABASE_NAME = "films.db"
        val DATABASE_VERSION = 1

        // Database creation sql statement
        private val CREATE_MOVIES = "create table $TABLE_MOVIES (" +
                "$COLUMN_ID integer primary key, " +
                "$COLUMN_TITLE text not null, " +
                "$COLUMN_OVERVIEW text not null, " +
                "$COLUMN_POPULARITY real, " +
                "$COLUMN_POSTER_PATH text, " +
                "$COLUMN_BACKDROP_PATH text, " +
                "$COLUMN_GENRES text, " +
                "$COLUMN_RELEASE_DATE text)"

        private val CREATE_SHOWS = "create table $TABLE_SHOWS (" +
                "$COLUMN_ID integer primary key, " +
                "$COLUMN_NAME text not null, " +
                "$COLUMN_OVERVIEW text not null, " +
                "$COLUMN_POPULARITY real, " +
                "$COLUMN_POSTER_PATH text, " +
                "$COLUMN_BACKDROP_PATH text, " +
                "$COLUMN_GENRES text, " +
                //                "$COLUMN_SEASONS_ID integer, " +
                "$COLUMN_FIRST_AIR_DATE text)"

        private val CREATE_SEASONS = "create table $TABLE_SEASONS (" +
                "$COLUMN_ID integer primary key, " +
                "$COLUMN_SEASON_NUMBER integer, " +
                "$COLUMN_EPISODE_COUNT integer, " +
                "$COLUMN_AIR_DATE text)"
    }

    fun addMovie(movie: Movie) {
        val values = ContentValues()
        values.put(COLUMN_ID, movie.id)
        values.put(COLUMN_TITLE, movie.title)
        values.put(COLUMN_OVERVIEW, movie.overview)
        values.put(COLUMN_RELEASE_DATE, movie.releaseDate)
        values.put(COLUMN_BACKDROP_PATH, movie.backdropPath)
        values.put(COLUMN_POSTER_PATH, movie.posterPath)
        values.put(COLUMN_POPULARITY, movie.popularity)
        values.put(COLUMN_GENRES, movie.genreIds.joinToString(","))
        val db = writableDatabase
        db.insert(TABLE_MOVIES, null, values)
        db.close()
    }

    fun getMovies(): Cursor {
        return readableDatabase.query(TABLE_MOVIES,
                arrayOf(
                        COLUMN_ID,
                        COLUMN_TITLE,
                        COLUMN_OVERVIEW,
                        COLUMN_POPULARITY,
                        COLUMN_POSTER_PATH,
                        COLUMN_BACKDROP_PATH,
                        COLUMN_GENRES,
                        COLUMN_RELEASE_DATE),
                null, null, null, null, null)
    }

    fun addShow(show: Show) {
        val values = ContentValues()
        values.put(COLUMN_ID, show.id)
        values.put(COLUMN_NAME, show.name)
        values.put(COLUMN_OVERVIEW, show.overview)
        values.put(COLUMN_FIRST_AIR_DATE, show.firstAirDate)
        values.put(COLUMN_BACKDROP_PATH, show.backdropPath)
        values.put(COLUMN_POSTER_PATH, show.posterPath)
        values.put(COLUMN_POPULARITY, show.popularity)
        values.put(COLUMN_GENRES, show.genreIds.joinToString(","))
        val db = writableDatabase
        db.insert(TABLE_SHOWS, null, values)
        db.close()
    }

    fun getShows(): Cursor {
        return readableDatabase.query(TABLE_SHOWS,
                arrayOf(
                        COLUMN_ID,
                        COLUMN_NAME,
                        COLUMN_OVERVIEW,
                        COLUMN_POPULARITY,
                        COLUMN_POSTER_PATH,
                        COLUMN_BACKDROP_PATH,
                        COLUMN_GENRES,
                        COLUMN_FIRST_AIR_DATE),
                null, null, null, null, null)
    }

    fun containsMovie(movie: Movie): Boolean {
        val c: Cursor? = readableDatabase.rawQuery("select $COLUMN_ID from $TABLE_MOVIES where $COLUMN_ID = ${movie.id}", null)
        if (c == null) return false
        c.close()
        return true

    }

}
