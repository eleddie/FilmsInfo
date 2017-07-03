package com.proyectosyntax.codingchallenge.utils

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.proyectosyntax.codingchallenge.R
import org.json.JSONObject
import java.net.URLEncoder
import java.util.*

object Requests {
    val language: String = if (Locale.getDefault().isO3Language == "spa") "es-US" else "en-US"
    fun getPopularMovies(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        val url = "${context.resources.getString(R.string.api_url)}movie/popular?api_key=${context.resources.getString(R.string.api_key)}&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun getTopRatedMovies(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        val url = "${context.resources.getString(R.string.api_url)}movie/top_rated?api_key=${context.resources.getString(R.string.api_key)}&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun getUpcomingMovies(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        val url = "${context.resources.getString(R.string.api_url)}movie/upcoming?api_key=${context.resources.getString(R.string.api_key)}&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun getPopularShows(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        val url = "${context.resources.getString(R.string.api_url)}tv/popular?api_key=${context.resources.getString(R.string.api_key)}&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun getTopRatedShows(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        val url = "${context.resources.getString(R.string.api_url)}tv/top_rated?api_key=${context.resources.getString(R.string.api_key)}&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun searchMovies(context: Context, query: String, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        val parameters = "query=${URLEncoder.encode(query, "utf-8")}"
        val url = "${context.resources.getString(R.string.api_url)}search/movie?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun searchShows(context: Context, query: String, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        val parameters = "query=${URLEncoder.encode(query, "utf-8")}"
        val url = "${context.resources.getString(R.string.api_url)}search/tv?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun filterMoviesByCategories(context: Context, categories: List<Pair<Int, String>>, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        var parameters = "with_genres="
        for (i in 0..categories.size - 1) {
            parameters += categories[i].first.toString() + ","
        }
        parameters = parameters.substring(0, parameters.length - 1)
        val url = "${context.resources.getString(R.string.api_url)}discover/movie?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun filterShowsByCategories(context: Context, categories: List<Pair<Int, String>>, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1): String {
        var parameters = "with_genres="
        for (i in 0..categories.size - 1) {
            parameters += categories[i].first.toString() + ","
        }
        parameters = parameters.substring(0, parameters.length - 1)
        val url = "${context.resources.getString(R.string.api_url)}discover/tv?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page&language=$language"
        makeRequest(context, url, listener, errorListener)
        return url
    }

    fun getSeasons(context: Context, showId: Long, listener: Response.Listener<JSONObject>) {
        val url = "${context.resources.getString(R.string.api_url)}tv/$showId?api_key=${context.resources.getString(R.string.api_key)}&language=$language"
        makeRequest(context, url, listener, null)
    }

    private fun makeRequest(context: Context, url: String, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener?): Request<JSONObject> {
        Log.i("URL Requested", url)
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener)
        return queue.add(request)
    }


}