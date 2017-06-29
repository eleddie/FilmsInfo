package com.proyectosyntax.codingchallenge.activities



import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.models.BaseFilm
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.models.Show
import com.proyectosyntax.codingchallenge.utils.ObjectSerializer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""

        val type = intent.extras.getInt("type")
        val serializedItem = intent.extras.getString("item")

        val item: BaseFilm
        if (type == 1) {
            item = ObjectSerializer.deserialize(serializedItem) as Movie
            name.text = item.title
            yearGenre.text = item.releaseDate
        } else {
            item = ObjectSerializer.deserialize(serializedItem) as Show
            name.text = item.name
            yearGenre.text = item.firstAirDate

        }
        rating.rating = (item.voteAverage / 10f) * 5
        overview.text = item.overview
        Picasso.with(this)
                .load("${resources.getString(R.string.image_url_780)}${item.backdropPath}")
                .placeholder(R.drawable.poster_placeholder)
                .into(backdrop)
        Picasso.with(this)
                .load("${resources.getString(R.string.image_url_500)}${item.posterPath}")
                .placeholder(R.drawable.poster_placeholder)
                .into(poster)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}
