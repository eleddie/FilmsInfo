package com.proyectosyntax.codingchallenge.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyectosyntax.codingchallenge.models.BaseFilm
import org.json.JSONObject
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.utils.*


class MoviesFragment : ListFragment() {

    val listener = Response.Listener<JSONObject> { response -> responseListener(response) }
    val errorListener = Response.ErrorListener { error -> handleError(error) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateType(CurrentState.Movie.type, CurrentState.Movie.page)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onLoadMoreItems() {
        if (!mListAdapter.isLoading) {
            mListAdapter.isLoading = true
            CurrentState.Movie.page++
            if (CurrentState.search != null)
                updateSearch(CurrentState.search!!, CurrentState.Movie.page)
            else if (!CurrentState.categories.isEmpty())
                updateCategories(CurrentState.categories.toList(), CurrentState.Movie.page)
            else
                updateType(CurrentState.Movie.type, CurrentState.Movie.page)
        }
    }

    override fun updateType(type: String, page: Int) {
        CurrentState.Movie.page = page
        CurrentState.Movie.type = type
        when (type) {
            CurrentState.TYPE_POPULAR -> {
                Requests.getPopularMovies(parentActivity, listener, errorListener, page)
            }
            CurrentState.TYPE_TOP_RATED -> {
                Requests.getTopRatedMovies(parentActivity, listener, errorListener, page)
            }
            CurrentState.TYPE_UPCOMING -> {
                Requests.getUpcomingMovies(parentActivity, listener, errorListener, page)
            }
        }
    }

    override fun updateSearch(query: String, page: Int) {
        if (CurrentState.Movie.page == 1)
            mListAdapter.setItems(ArrayList())
        Requests.searchMovies(parentActivity, query, listener, errorListener, page)
    }

    override fun updateCategories(categories: List<Pair<Int, String>>, page: Int) {
        Requests.filterMoviesByCategories(parentActivity, categories, listener, errorListener, page)
    }

    override fun responseListener(response: JSONObject?): ArrayList<BaseFilm?> {
        if (mSwipeRefreshLayout.isRefreshing) {
            onItemsLoadComplete()
        }
        val results = response?.getString("results")
        if (results != null) {
            val gson = Gson()
            val items: ArrayList<BaseFilm?> = gson.fromJson(results, object : TypeToken<ArrayList<Movie>>() {}.type)
            if (CurrentState.Movie.page == 1) {
                mListAdapter.setItems(items)
            } else {
                mListAdapter.isLoading = false
                mListAdapter.addItems(items)
            }
        }
        return mListAdapter.getItems()
    }

    override fun handleError(error: VolleyError?) {
        Log.e("Error Movie Req", error.toString())
    }

    override fun refreshItems() {
        CurrentState.Movie.page = 1
        if (CurrentState.search != null)
            updateSearch(CurrentState.search!!, CurrentState.Movie.page)
        else if (!CurrentState.categories.isEmpty())
            updateCategories(CurrentState.categories.toList(), CurrentState.Movie.page)
        else
            updateType(CurrentState.Movie.type, CurrentState.Movie.page)
    }

    override fun onItemsLoadComplete() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

}
