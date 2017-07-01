package com.proyectosyntax.codingchallenge.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.proyectosyntax.codingchallenge.R
import org.json.JSONObject
import java.net.URLEncoder

object Requests {
    fun getPopularMovies(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        val url = "${context.resources.getString(R.string.api_url)}movie/popular?api_key=${context.resources.getString(R.string.api_key)}&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun getTopRatedMovies(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        val url = "${context.resources.getString(R.string.api_url)}movie/top_rated?api_key=${context.resources.getString(R.string.api_key)}&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun getUpcomingMovies(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        val url = "${context.resources.getString(R.string.api_url)}movie/upcoming?api_key=${context.resources.getString(R.string.api_key)}&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun getPopularShows(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        val url = "${context.resources.getString(R.string.api_url)}tv/popular?api_key=${context.resources.getString(R.string.api_key)}&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun getTopRatedShows(context: Context, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        val url = "${context.resources.getString(R.string.api_url)}tv/top_rated?api_key=${context.resources.getString(R.string.api_key)}&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun searchMovies(context: Context, query: String, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        val parameters = "query=${URLEncoder.encode(query, "utf-8")}"
        val url = "${context.resources.getString(R.string.api_url)}search/movie?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun searchShows(context: Context, query: String, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        val parameters = "query=${URLEncoder.encode(query, "utf-8")}"
        val url = "${context.resources.getString(R.string.api_url)}search/tv?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun filterMoviesByCategories(context: Context, categories: List<Pair<Int, String>>, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        var parameters = "with_genres="
        for (i in 0..categories.size - 1) {
            parameters += categories[i].first.toString() + ","
        }
        parameters = parameters.substring(0, parameters.length - 1)
        val url = "${context.resources.getString(R.string.api_url)}discover/movie?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    fun filterShowsByCategories(context: Context, categories: List<Pair<Int, String>>, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener, page: Int = 1) {
        var parameters = "with_genres="
        for (i in 0..categories.size - 1) {
            parameters += categories[i].first.toString() + ","
        }
        parameters = parameters.substring(0, parameters.length - 1)
        val url = "${context.resources.getString(R.string.api_url)}discover/tv?api_key=${context.resources.getString(R.string.api_key)}&$parameters&page=$page"
        makeRequest(context, url, listener, errorListener)
    }

    private fun makeRequest(context: Context, url: String, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener) {
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener)
        queue.add(request)
    }

}