package com.proyectosyntax.codingchallenge.fragments


import android.app.Activity
import android.content.Context
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
import com.proyectosyntax.codingchallenge.models.Show
import com.proyectosyntax.codingchallenge.utils.ObjectSerializer
import com.proyectosyntax.codingchallenge.utils.RecyclerViewListener
import com.proyectosyntax.codingchallenge.utils.SpacesItemDecoration
import org.json.JSONObject
import com.proyectosyntax.codingchallenge.R
import java.net.URLEncoder


class ShowsFragment : Fragment() {
    lateinit var mListAdapter: ListAdapter
    lateinit var titlesList: ShimmerRecyclerView
    lateinit var parentActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = activity
        mListAdapter = ListAdapter(parentActivity)
        updateList(arguments.getString("extra"))
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
                intent.putExtra("item", ObjectSerializer.serialize(tappedItem as Show))
                intent.putExtra("type", 2)
                startActivity(intent)
            }


        }))
        titlesList.layoutManager = GridLayoutManager(parentActivity, 2)

        titlesList.adapter = mListAdapter
        return rootView
    }

    fun updateList(extra: String?) {
        val queue: RequestQueue = Volley.newRequestQueue(parentActivity)
        val url = "${parentActivity!!.resources.getString(R.string.api_url)}tv/$extra?api_key=${parentActivity!!.resources.getString(R.string.api_key)}"
        val request = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response -> connectionEstablished(response) },
                Response.ErrorListener { error -> handleError(error) }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }
        queue.add(request)
    }

    fun search(keyword: String = "") {
        val parameters = "query=${URLEncoder.encode(keyword, "utf-8")}"
        val url = "${parentActivity.resources.getString(R.string.api_url)}search/tv?api_key=${parentActivity.resources.getString(R.string.api_key)}&$parameters"

        Log.i("URL", url)

        mListAdapter.setItems(ArrayList())
        val queue: RequestQueue = Volley.newRequestQueue(parentActivity)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response -> connectionEstablished(response) },
                Response.ErrorListener { error -> handleError(error) })
        queue.add(request)
    }

    fun filterCategories(categories: List<Pair<Int, String>>) {
        var parameters = "with_genres="
        for (i in 0..categories.size - 1) {
            parameters += categories[i].first.toString() + ","
        }
        parameters = parameters.substring(0, parameters.length - 1)

        val url = "${parentActivity.resources.getString(R.string.api_url)}discover/tv?api_key=${parentActivity.resources.getString(R.string.api_key)}&$parameters"

        Log.i("URL", url)

        mListAdapter.setItems(ArrayList())
        val queue: RequestQueue = Volley.newRequestQueue(parentActivity)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response -> connectionEstablished(response) },
                Response.ErrorListener { error -> handleError(error) })
        queue.add(request)
    }

    private fun connectionEstablished(response: org.json.JSONObject?) {
        val results = response?.getString("results")
        Log.i("Results show", results)
        if (results != null) {
            val gson = Gson()
            val items: ArrayList<BaseFilm?> = gson.fromJson(results, object : TypeToken<ArrayList<Show>>() {}.type)
            Log.i("Items show", items.toString())
            mListAdapter.setItems(items)
        }
    }

    private fun handleError(error: VolleyError?) {
        Log.e("Error Tv Req", error.toString())
    }

    companion object {
        var parentActivity: Context? = null

        fun newInstance(pActivity: Context, extra: String): ShowsFragment {
            android.util.Log.i("new Intance Show", "Called")
            parentActivity = pActivity
            val fragment = ShowsFragment()
            val args = Bundle()
            args.putString("extra", extra)
            fragment.arguments = args
            return fragment
        }
    }
}
