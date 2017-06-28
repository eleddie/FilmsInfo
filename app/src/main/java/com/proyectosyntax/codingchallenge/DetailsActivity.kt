package com.proyectosyntax.codingchallenge

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.proyectosyntax.codingchallenge.Models.Movie
import com.proyectosyntax.codingchallenge.Models.Show
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""

        val type = intent.extras.getInt("type")
        val serializedItem = intent.extras.getString("item")

        if (type == 1) {
            val item: Movie = ObjectSerializer.deserialize(serializedItem) as Movie
            Picasso.with(this)
                    .load("${resources.getString(R.string.image_url)}${item.posterPath}")
                    .placeholder(R.drawable.poster_placeholder)
                    .into(movieImage)
            name.text = item.title
            yearGenre.text = item.releaseDate
            rating.rating = item.voteAverage
            overview.text = item.overview
        } else {
            val item: Show = ObjectSerializer.deserialize(serializedItem) as Show
            Picasso.with(this)
                    .load("${resources.getString(R.string.image_url)}${item.posterPath}")
                    .placeholder(R.drawable.poster_placeholder)
                    .into(movieImage)
            name.text = item.name
            yearGenre.text = item.firstAirDate
            rating.rating = item.voteAverage
            overview.text = item.overview
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}
