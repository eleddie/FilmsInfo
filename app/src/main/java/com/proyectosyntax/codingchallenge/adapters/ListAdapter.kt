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
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private var items: ArrayList<BaseFilm?> = ArrayList()
    var isLoading = false

    init {
        items.add(null)
    }

    companion object {
        val VIEW_TYPE_ITEM = 0
        val VIEW_TYPE_LOADING = 1
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoaderHolder) {

        } else if (holder is TitleHolder) {
            val current = items[position]

            if (current is Movie) {
                holder.title.text = current.title
                holder.year.text = current.releaseDate

            } else if (current is Show) {
                holder.title.text = current.name
                holder.year.text = current.firstAirDate

            }

            Picasso.with(context)
                    .load("${context.resources.getString(R.string.image_url_500)}${current?.posterPath}")
                    .placeholder(R.drawable.poster_placeholder)
                    .into(holder.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val view: View = inflater.inflate(R.layout.item, parent, false)
            return TitleHolder(view)
        } else {
            val view: View = inflater.inflate(R.layout.item_loading, parent, false)
            return LoaderHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.movieImage) as ImageView
        var title: TextView = itemView.findViewById(R.id.movieTitle) as TextView
        var year: TextView = itemView.findViewById(R.id.movieYear) as TextView
    }

    inner class LoaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setItems(items: ArrayList<BaseFilm?>) {
        this.items = items
        items.add(null)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): BaseFilm? {
        return items[position]
    }

    fun addItems(itemsToAdd: ArrayList<BaseFilm?>) {
        val last = items.size - 1
        items.removeAt(last)
        items.addAll(itemsToAdd)
        items.add(null)
        notifyDataSetChanged()
    }

    fun getItems() :ArrayList<BaseFilm?>{
        return items
    }

}