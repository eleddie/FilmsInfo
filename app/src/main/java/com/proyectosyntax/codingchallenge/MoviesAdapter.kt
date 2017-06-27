package com.proyectosyntax.codingchallenge

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class MoviesAdapter(context: Context, private var items: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.MyHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        val current: Movie = items[position]
//        holder!!.imageId.setImageResource(current.imageId)
        holder!!.title.text = current.title
        holder.year.text = current.year.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val view: View = inflater.inflate(R.layout.movie_item, parent, false)
        val holder = MyHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageId: ImageView = itemView.findViewById(R.id.movieImage) as ImageView
        var title: TextView = itemView.findViewById(R.id.movieTitle) as TextView
        var year: TextView = itemView.findViewById(R.id.movieYear) as TextView
    }

    data class Movie(var title:String, var year:Int)
}