package com.proyectosyntax.codingchallenge.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.proyectosyntax.codingchallenge.models.BaseFilm
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.models.Show
import com.proyectosyntax.codingchallenge.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

class ListAdapter(var context: Context, private var items: ArrayList<BaseFilm>) : RecyclerView.Adapter<ListAdapter.MyHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        val current = items[position]

        if (current is Movie) {
            holder!!.title.text = current.title
            holder.year.text = current.releaseDate

        } else if (current is Show) {
            holder!!.title.text = current.name
            holder.year.text = current.firstAirDate

        }

        Picasso.with(context)
                .load("${context.resources.getString(R.string.image_url_500)}${current.posterPath}")
                .placeholder(R.drawable.poster_placeholder)
                .into(holder!!.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val view: View = inflater.inflate(R.layout.item, parent, false)
        val holder = MyHolder(view)
        return holder
    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.movieImage) as ImageView
        var title: TextView = itemView.findViewById(R.id.movieTitle) as TextView
        var year: TextView = itemView.findViewById(R.id.movieYear) as TextView
    }

    fun setItems(items: ArrayList<BaseFilm>) {
        this.items = items
    }

    fun getItem(position: Int): BaseFilm {
        return items[position]
    }
}