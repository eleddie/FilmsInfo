package com.proyectosyntax.codingchallenge.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyectosyntax.codingchallenge.models.BaseFilm
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.utils.CurrentState
import com.proyectosyntax.codingchallenge.utils.Requests
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMCondition
import org.json.JSONObject


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
        if (CurrentState.Movie.page == 1)
            titlesList.post { mListAdapter.setItems(ArrayList()) }
        Requests.filterMoviesByCategories(parentActivity, categories, listener, errorListener, page)
    }

    override fun responseListener(response: JSONObject?): ArrayList<BaseFilm?> {
        if (mSwipeRefreshLayout.isRefreshing) {
            onItemsLoadComplete()
        }
        val results = response?.getString("results")
        Log.i("Results", results.toString())
        if (results != null) {
            empty.visibility = View.GONE
            titlesList.visibility = View.VISIBLE
            val gson = Gson()
            val items: ArrayList<BaseFilm?> = gson.fromJson(results, object : TypeToken<ArrayList<Movie>>() {}.type)
            if (items.isEmpty()) {
                empty.visibility = View.VISIBLE
                titlesList.visibility = View.GONE
            } else {
                AsyncSave(parentActivity.filesDir.absolutePath).execute(items)
                if (CurrentState.Movie.page == 1) {
                    mListAdapter.setItems(items)
                } else {
                    mListAdapter.isLoading = false
                    mListAdapter.addItems(items)
                }
            }
        }
        return mListAdapter.getItems()
    }

    override fun handleError(error: VolleyError?) {
        Log.e("Error Movie Req", error.toString())
        val appPath = parentActivity.filesDir.absolutePath
        val pultusORM: PultusORM = PultusORM("films.db", appPath)
        val categoriesSelected = CurrentState.getCategoriesString().split(",")
        val conditionBuilder: PultusORMCondition.Builder = PultusORMCondition.Builder().contains("genreIds", "")
        for (it in categoriesSelected) {
            conditionBuilder.and().contains("genreIds", it)
        }
        if (CurrentState.search != null) {
            conditionBuilder.and().contains("title", CurrentState.search!!)
        }
        val condition = conditionBuilder.build()
        val items = pultusORM.find(Movie.MovieSQLite(), condition)
        val convertedItems = ArrayList<BaseFilm?>()
        if (items.isEmpty()) {
            empty.visibility = View.VISIBLE
            titlesList.visibility = View.GONE
        } else {
            for (item in items)
                convertedItems.add((item as Movie.MovieSQLite).fromSQLite())
            mListAdapter.setItems(convertedItems)
        }
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

    class AsyncSave(var appPath: String) : AsyncTask<ArrayList<BaseFilm?>, Int, Boolean>() {
        override fun doInBackground(vararg list: ArrayList<BaseFilm?>): Boolean {
            val pultusORM: PultusORM = PultusORM("films.db", appPath)
            var movieSQLite: Movie.MovieSQLite
            for ((index, it) in list[0].withIndex()) {
                if (it == null) continue
                movieSQLite = (it as Movie).toSQLite()
                val condition: PultusORMCondition = PultusORMCondition.Builder().eq("id", movieSQLite.id).build()
                if (pultusORM.find(movieSQLite, condition).size == 0) {
                    val saved = pultusORM.save(movieSQLite)
                    Log.i("Saved Movie $index", saved.toString())
                }
            }
            pultusORM.close()
            return true
        }

    }

}
