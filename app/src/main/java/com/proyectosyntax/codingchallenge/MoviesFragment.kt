package com.proyectosyntax.codingchallenge

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
import org.json.JSONObject
import com.proyectosyntax.codingchallenge.Models.Movie


class MoviesFragment : Fragment(){

    var mListAdapter: ListAdapter? = null
    var titlesList: ShimmerRecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val url = "${activity.resources.getString(R.string.api_url)}movie/${arguments.getString("extra")}?api_key=${activity.resources.getString(R.string.api_key)}"
        Log.i("URL", url)
        val request = object : JsonObjectRequest(Request.Method.GET, url, null,
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


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_list, container, false)
        titlesList = rootView.findViewById(R.id.titlesList) as ShimmerRecyclerView
        titlesList!!.showShimmerAdapter()
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        titlesList!!.addItemDecoration(SpacesItemDecoration(spacingInPixels))


        titlesList!!.addOnItemTouchListener(RecyclerViewListener(context, titlesList!!, object : RecyclerViewListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                val tappedItem = mListAdapter?.getItem(position)
                val intent: Intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("item", ObjectSerializer.serialize(tappedItem as Movie))
                intent.putExtra("type", 1)
                startActivity(intent)
            }


        }))
        titlesList!!.layoutManager = GridLayoutManager(activity, 2)
        mListAdapter = ListAdapter(activity, ArrayList<Any>())
        titlesList!!.adapter = mListAdapter
        return rootView
    }

    private fun connectionEstablished(response: JSONObject?) {
        val results = response?.getString("results")
        Log.i("Results", results)
        if (results != null) {
            val gson = Gson()
            val items: ArrayList<Any> = gson.fromJson(results, object : TypeToken<ArrayList<Movie>>() {}.type)
            mListAdapter!!.setItems(items)
        }
    }

    private fun handleError(error: VolleyError?) {
        Log.i("Error", error.toString())
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
