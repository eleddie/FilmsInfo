package com.proyectosyntax.codingchallenge.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.VolleyError
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.activities.DetailsActivity
import com.proyectosyntax.codingchallenge.adapters.ListAdapter
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.utils.*
import org.json.JSONObject

abstract class ListFragment : Fragment() {
    lateinit var mListAdapter: ListAdapter
    lateinit var titlesList: ShimmerRecyclerView
    lateinit var parentActivity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = activity
        mListAdapter = ListAdapter(parentActivity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_list, container, false)
        titlesList = rootView.findViewById(R.id.titlesList) as ShimmerRecyclerView
        titlesList.showShimmerAdapter()
        titlesList.addItemDecoration(SpacesItemDecoration())

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

    abstract fun updateType(type: String, page: Int)
    abstract fun updateSearch(query: String, page: Int)
    abstract fun updateCategories(categories: List<Pair<Int, String>>, page: Int)
    abstract fun responseListener(response: JSONObject?)
    abstract fun handleError(error: VolleyError?)
}