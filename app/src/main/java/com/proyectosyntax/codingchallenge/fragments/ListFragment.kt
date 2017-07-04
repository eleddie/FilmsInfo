package com.proyectosyntax.codingchallenge.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.VolleyError
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.activities.DetailsActivity
import com.proyectosyntax.codingchallenge.adapters.ListAdapter
import com.proyectosyntax.codingchallenge.models.BaseFilm
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.models.Show
import com.proyectosyntax.codingchallenge.utils.ObjectSerializer
import com.proyectosyntax.codingchallenge.utils.RecyclerViewClickListener
import com.proyectosyntax.codingchallenge.utils.RecyclerViewLoadMoreListener
import org.json.JSONObject

abstract class ListFragment : Fragment() {
    lateinit var mListAdapter: ListAdapter
    lateinit var titlesList: RecyclerView
    lateinit var empty: TextView
    lateinit var parentActivity: Activity
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = activity
        mListAdapter = ListAdapter(parentActivity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_list, container, false)
        titlesList = rootView.findViewById(R.id.titlesList) as RecyclerView
        empty = rootView.findViewById(R.id.empty) as TextView
        titlesList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = 2
                outRect.right = 2
                outRect.bottom = 2
                outRect.top = if (parent.getChildLayoutPosition(view) == 0) 2 else 0
            }
        })

        titlesList.addOnItemTouchListener(RecyclerViewClickListener(context, object : RecyclerViewClickListener.ClickListener {
            override fun onClick(view: View, position: Int) {
                val tappedItem = mListAdapter.getItem(position)
                val intent: Intent = Intent(context, DetailsActivity::class.java)
                Log.i("Tapped type:", (tappedItem is Movie).toString())
                if (tappedItem is Movie) {
                    intent.putExtra("item", ObjectSerializer.serialize(tappedItem))
                    intent.putExtra("type", 1)
                } else if (tappedItem is Show) {
                    intent.putExtra("item", ObjectSerializer.serialize(tappedItem))
                    intent.putExtra("type", 2)
                }
                startActivity(intent)
            }
        }))

        mSwipeRefreshLayout = rootView.findViewById(R.id.mSwipeRefreshLayout) as SwipeRefreshLayout

        mSwipeRefreshLayout.setOnRefreshListener({
            refreshItems()
        })

        titlesList.adapter = mListAdapter


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
            override fun onLoadMore() = onLoadMoreItems()
        }))

        return rootView
    }

    abstract fun refreshItems()
    abstract fun onItemsLoadComplete()
    abstract fun onLoadMoreItems()
    abstract fun updateType(type: String, page: Int)
    abstract fun updateSearch(query: String, page: Int)
    abstract fun updateCategories(categories: List<Pair<Int, String>>, page: Int)
    abstract fun responseListener(response: JSONObject?): ArrayList<BaseFilm?>
    abstract fun handleError(error: VolleyError?)
}