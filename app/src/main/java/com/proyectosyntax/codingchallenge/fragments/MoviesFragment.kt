package com.proyectosyntax.codingchallenge.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyectosyntax.codingchallenge.activities.DetailsActivity
import com.proyectosyntax.codingchallenge.adapters.ListAdapter
import com.proyectosyntax.codingchallenge.models.BaseFilm
import org.json.JSONObject
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.utils.ObjectSerializer
import com.proyectosyntax.codingchallenge.utils.RecyclerViewListener
import com.proyectosyntax.codingchallenge.utils.SpacesItemDecoration


class MoviesFragment : Fragment() {

    lateinit var mListAdapter: ListAdapter
    lateinit var titlesList: ShimmerRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListAdapter = ListAdapter(activity, ArrayList<BaseFilm>())
        updateList(arguments.getString("extra"))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_list, container, false)
        titlesList = rootView.findViewById(R.id.titlesList) as ShimmerRecyclerView
        titlesList.showShimmerAdapter()
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        titlesList.addItemDecoration(SpacesItemDecoration(spacingInPixels))


        titlesList.addOnItemTouchListener(RecyclerViewListener(context, titlesList, object : RecyclerViewListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                val tappedItem = mListAdapter.getItem(position)
                val intent: Intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("item", ObjectSerializer.serialize(tappedItem as Movie))
                intent.putExtra("type", 1)
                startActivity(intent)
            }


        }))
        titlesList.layoutManager = GridLayoutManager(activity, 2)
        titlesList.adapter = mListAdapter
        return rootView
    }

    fun updateList(extra: String) {
        mListAdapter.setItems(ArrayList())
        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val url = "${activity.resources.getString(R.string.api_url)}movie/$extra?api_key=${activity.resources.getString(R.string.api_key)}"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response -> connectionEstablished(response) },
                Response.ErrorListener { error -> handleError(error) })
        queue.add(request)
    }

    fun search(categories: List<Pair<Int, String>>) {

        var parameters = "with_genres="
        for (i in 0..categories.size - 1) {
            parameters += categories[i].first.toString() + ","
        }
        parameters = parameters.substring(0, parameters.length - 1)
        val url = "${activity.resources.getString(R.string.api_url)}discover/movie?api_key=${activity.resources.getString(R.string.api_key)}&$parameters"

        Log.i("URL", url)
        Log.i("Genres", categories.toString())

        mListAdapter.setItems(ArrayList())
        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response -> connectionEstablished(response) },
                Response.ErrorListener { error -> handleError(error) })
        queue.add(request)
    }

    private fun connectionEstablished(response: JSONObject?) {
        val results = response?.getString("results")
        Log.i("Results movie", results)
        if (results != null) {
            val gson = Gson()
            val items: ArrayList<BaseFilm> = gson.fromJson(results, object : TypeToken<ArrayList<Movie>>() {}.type)
            mListAdapter.setItems(items)
            mListAdapter.notifyDataSetChanged()
        }
    }

    private fun handleError(error: VolleyError?) {
        Log.e("Error Movie Req", error.toString())
    }

    companion object {
        fun newInstance(extra: String): MoviesFragment {
            val fragment = MoviesFragment()
            val args = Bundle()
            args.putString("extra", extra)
            fragment.arguments = args
            return fragment
        }
    }
}