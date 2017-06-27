package com.proyectosyntax.codingchallenge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class MoviesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_movies, container, false)
        val titlesList = rootView.findViewById(R.id.titlesList) as RecyclerView

        val titles = ArrayList<MoviesAdapter.Movie>()
        titlesList.layoutManager = GridLayoutManager(activity, 2)
        titlesList.adapter = MoviesAdapter(activity,titles)
        return rootView
    }


    companion object {
        fun newInstance(): MoviesFragment {
            val fragment = MoviesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
