package com.proyectosyntax.codingchallenge.utils

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager


class RecyclerViewLoadMoreListener(var layoutManager: LinearLayoutManager, var scrollListener: ScrollListener) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        if (totalItemCount <= (lastVisibleItem + 5)) {
            scrollListener.onLoadMore()
        }
    }

    interface ScrollListener {
        fun onLoadMore()
    }
}