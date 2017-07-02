package com.proyectosyntax.codingchallenge.activities


import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
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

    lateinit var categoriesMap: HashMap<Int, String>

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""

        val preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val categoriesSaved = preferences.getString("categories", null)
        categoriesMap = ObjectSerializer.deserialize(categoriesSaved) as HashMap<Int, String>

        val type = intent.extras.getInt("type")
        val serializedItem = intent.extras.getString("item")

        val title:String

        val item: BaseFilm
        if (type == 1) {
            item = ObjectSerializer.deserialize(serializedItem) as Movie
            name.text = item.title
            year.text = item.releaseDate
            title = item.title
        } else {
            item = ObjectSerializer.deserialize(serializedItem) as Show
            name.text = item.name
            year.text = item.firstAirDate
            title = item.name

        }

        var genresOfMovie = ""
        for (i in 0..item.genreIds.size - 1) {
            genresOfMovie += categoriesMap[item.genreIds[i]] + " | "
        }
        genresOfMovie = genresOfMovie.substring(0, genresOfMovie.length - 3)
        genres.text = genresOfMovie

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

        val collapsingToolbarLayout: CollapsingToolbarLayout = findViewById(R.id.toolbar_layout_details) as CollapsingToolbarLayout
        app_bar_details.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = title
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " "
                    isShow = false
                }
            }
        })

    }
}
