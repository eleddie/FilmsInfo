package com.proyectosyntax.codingchallenge.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.*
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyectosyntax.codingchallenge.activities.DetailsActivity
import com.proyectosyntax.codingchallenge.adapters.ListAdapter
import com.proyectosyntax.codingchallenge.models.BaseFilm
import org.json.JSONObject
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.utils.*


class MoviesFragment : Fragment() {

    lateinit var mListAdapter: ListAdapter
    lateinit var titlesList: ShimmerRecyclerView
    lateinit var parentActivity: Activity

    val listener = Response.Listener<JSONObject> { response -> responseListener(response) }
    val errorListener = Response.ErrorListener { error -> handleError(error) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = activity
        mListAdapter = ListAdapter(parentActivity)
        updateType(CurrentState.Movie.type, CurrentState.Movie.page)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_list, container, false)
        titlesList = rootView.findViewById(R.id.titlesList) as ShimmerRecyclerView
        titlesList.showShimmerAdapter()
        titlesList.addItemDecoration(SpacesItemDecoration())
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
                    CurrentState.Movie.page++
                    if (CurrentState.search != null)
                        updateSearch(CurrentState.search!!, CurrentState.Movie.page)
                    else if (!CurrentState.categories.isEmpty())
                        updateCategories(CurrentState.categories.toList(), CurrentState.Movie.page)
                    else
                        updateType(CurrentState.Movie.type, CurrentState.Movie.page)
                }
            }
        }))

        titlesList.addOnItemTouchListener(RecyclerViewListener(context, object : RecyclerViewListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                val tappedItem = mListAdapter.getItem(position)
                val intent: Intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("item", ObjectSerializer.serialize(tappedItem as Movie))
                intent.putExtra("type", 1)
                startActivity(intent)
            }


        }))

        titlesList.adapter = mListAdapter
        return rootView
    }


    fun updateType(type: String, page: Int) {
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

    fun updateSearch(query: String, page: Int) {
        if (CurrentState.Movie.page == 1)
            mListAdapter.setItems(ArrayList())
        Requests.searchMovies(parentActivity, query, listener, errorListener, page)
    }

    fun updateCategories(categories: List<Pair<Int, String>>, page: Int = 1) {
        Requests.filterMoviesByCategories(parentActivity, categories, listener, errorListener, page)
    }

    fun responseListener(response: JSONObject?) {
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
    }

    private fun handleError(error: VolleyError?) {
        Log.e("Error Movie Req", error.toString())
    }

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

}
