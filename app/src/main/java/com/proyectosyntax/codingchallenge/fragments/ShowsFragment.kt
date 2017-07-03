package com.proyectosyntax.codingchallenge.fragments

import android.content.Context
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
import com.proyectosyntax.codingchallenge.models.Show
import com.proyectosyntax.codingchallenge.utils.CurrentState
import com.proyectosyntax.codingchallenge.utils.Requests
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMCondition
import org.json.JSONObject


class ShowsFragment : ListFragment() {

    val listener = Response.Listener<JSONObject> { response -> responseListener(response) }
    val errorListener = Response.ErrorListener { error -> handleError(error) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateType(CurrentState.Show.type, CurrentState.Show.page)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onLoadMoreItems() {
        if (!mListAdapter.isLoading) {
            mListAdapter.isLoading = true
            CurrentState.Show.page++
            if (CurrentState.search != null)
                updateSearch(CurrentState.search!!, CurrentState.Show.page)
            else if (!CurrentState.categories.isEmpty())
                updateCategories(CurrentState.categories.toList(), CurrentState.Show.page)
            else
                updateType(CurrentState.Show.type, CurrentState.Show.page)
        }
    }

    override fun updateType(type: String, page: Int) {
        CurrentState.Show.page = page
        CurrentState.Show.type = type
        when (type) {
            CurrentState.TYPE_POPULAR -> {
                Requests.getPopularShows(parentActivity, listener, errorListener, page)
            }
            CurrentState.TYPE_TOP_RATED -> {
                Requests.getTopRatedShows(parentActivity, listener, errorListener, page)
            }
        }
    }

    override fun updateSearch(query: String, page: Int) {
        if (CurrentState.Show.page == 1)
            mListAdapter.setItems(ArrayList())
        Requests.searchShows(parentActivity, query, listener, errorListener, page)
    }

    override fun updateCategories(categories: List<Pair<Int, String>>, page: Int) {
        if (CurrentState.Show.page == 1)
            titlesList.post { mListAdapter.setItems(ArrayList()) }
        Requests.filterShowsByCategories(parentActivity, categories, listener, errorListener, page)
    }

    override fun responseListener(response: JSONObject?): ArrayList<BaseFilm?> {
        if (mSwipeRefreshLayout.isRefreshing) {
            onItemsLoadComplete()
        }
        val results = response?.getString("results")
        if (results != null) {
            val gson = Gson()
            val items: ArrayList<BaseFilm?> = gson.fromJson(results, object : TypeToken<ArrayList<Show>>() {}.type)
            AsyncSave(parentActivity.filesDir.absolutePath).execute(items)
//            async {await { saveAsync(items, parentActivity) }}

            if (CurrentState.Show.page == 1) {
                mListAdapter.setItems(items)
            } else {
                mListAdapter.isLoading = false
                mListAdapter.addItems(items)
            }
        }
        return mListAdapter.getItems()
    }

    private fun saveAsync(items: ArrayList<BaseFilm?>, context: Context) {
        val appPath = context.filesDir.absolutePath
        val pultusORM: PultusORM = PultusORM("films.db", appPath)
        var showSQLite: Show.ShowSQLite
        for ((index, it) in items.withIndex()) {
            showSQLite = (it as Show).toSQLite()
            val condition: PultusORMCondition = PultusORMCondition.Builder().eq("id", showSQLite.id).build()
            if (pultusORM.find(showSQLite, condition).size == 0) {
                val saved = pultusORM.save(showSQLite)
                Log.i("Saved Show $index", saved.toString())
            }
        }
        pultusORM.close()
    }

    override fun handleError(error: VolleyError?) {
        Log.e("Error Show Req", error.toString())
        val appPath = parentActivity.filesDir.absolutePath
        val pultusORM: PultusORM = PultusORM("films.db", appPath)
        val categoriesSelected = CurrentState.getCategoriesString().split(",")
        val conditionBuilder: PultusORMCondition.Builder = PultusORMCondition.Builder().contains("genreIds", "")
        for (it in categoriesSelected) {
            conditionBuilder.and().contains("genreIds", it)
        }
        val condition = conditionBuilder.build()

        val items = pultusORM.find(Show.ShowSQLite(), condition)
        val convertedItems = ArrayList<BaseFilm?>()
        for(item in items)
            convertedItems.add((item as Show.ShowSQLite).fromSQLite())
        mListAdapter.setItems(convertedItems)
    }

    override fun refreshItems() {
        CurrentState.Show.page = 1
        if (CurrentState.search != null)
            updateSearch(CurrentState.search!!, CurrentState.Show.page)
        else if (!CurrentState.categories.isEmpty())
            updateCategories(CurrentState.categories.toList(), CurrentState.Show.page)
        else
            updateType(CurrentState.Show.type, CurrentState.Show.page)
    }

    override fun onItemsLoadComplete() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    companion object {
        fun newInstance(): ShowsFragment {
            return ShowsFragment()
        }
    }

    class AsyncSave(var appPath: String) : AsyncTask<ArrayList<BaseFilm?>, Int, Boolean>() {
        override fun doInBackground(vararg list: ArrayList<BaseFilm?>): Boolean {
            val pultusORM: PultusORM = PultusORM("films.db", appPath)
            var movieSQLite: Show.ShowSQLite
            for ((index, it) in list[0].withIndex()) {
                if (it == null) continue
                Log.i("Searching in DB", it.toString())
                movieSQLite = (it as Show).toSQLite()
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
