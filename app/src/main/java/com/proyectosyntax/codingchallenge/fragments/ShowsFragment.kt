package com.proyectosyntax.codingchallenge.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyectosyntax.codingchallenge.adapters.ListAdapter
import com.proyectosyntax.codingchallenge.models.BaseFilm
import org.json.JSONObject
import com.proyectosyntax.codingchallenge.models.Show
import com.proyectosyntax.codingchallenge.utils.*


class ShowsFragment : ListFragment() {

    val listener = Response.Listener<JSONObject> { response -> responseListener(response) }
    val errorListener = Response.ErrorListener { error -> handleError(error) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateType(CurrentState.Show.type, CurrentState.Show.page)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        val layoutManager = GridLayoutManager(parentActivity, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (mListAdapter.getItemViewType(position)) {
                    ListAdapter.VIEW_TYPE_ITEM -> return 1
                    else -> return 2
                }
            }
        }

        titlesList.layoutManager = layoutManager
        titlesList.addOnScrollListener(RecyclerViewLoadMoreListener(layoutManager, object : RecyclerViewLoadMoreListener.ScrollListener {
            override fun onLoadMore() {
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
        }))
        return rootView
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
        Requests.filterShowsByCategories(parentActivity, categories, listener, errorListener, page)
    }

    override fun responseListener(response: JSONObject?) {
        val results = response?.getString("results")
        if (results != null) {
            val gson = Gson()
            val items: ArrayList<BaseFilm?> = gson.fromJson(results, object : TypeToken<ArrayList<Show>>() {}.type)
            if (CurrentState.Show.page == 1) {
                mListAdapter.setItems(items)
            } else {
                mListAdapter.isLoading = false
                mListAdapter.addItems(items)
            }
        }
    }

    override fun handleError(error: VolleyError?) {
        Log.e("Error Show Req", error.toString())
    }

    companion object {
        fun newInstance(): ShowsFragment {
            return ShowsFragment()
        }
    }

}
