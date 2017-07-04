package com.proyectosyntax.codingchallenge.activities


import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.models.BaseFilm
import com.proyectosyntax.codingchallenge.models.Movie
import com.proyectosyntax.codingchallenge.models.Show
import com.proyectosyntax.codingchallenge.utils.ObjectSerializer
import com.proyectosyntax.codingchallenge.utils.Requests
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.json.JSONObject


class DetailsActivity : AppCompatActivity() {
    lateinit var categoriesMap: HashMap<Int, String>
    val listener = Response.Listener<JSONObject> { response -> responseListener(response) }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""
        seasonsLayout.visibility = View.GONE
        val preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val categoriesSaved = preferences.getString("categories", null)
        categoriesMap = ObjectSerializer.deserialize(categoriesSaved) as HashMap<Int, String>

        val type = intent.extras.getInt("type")
        val serializedItem = intent.extras.getString("item")

        val title: String

        val item: BaseFilm
        if (type == 1) {
            item = ObjectSerializer.deserialize(serializedItem) as Movie
            name.text = item.title
            year.text = item.releaseDate
            title = item.title!!
        } else {
            item = ObjectSerializer.deserialize(serializedItem) as Show
            name.text = item.name
            year.text = item.firstAirDate
            title = item.name
            getSeasons(item)
        }

        var genresOfMovie = ""
        for (i in 0..item.genreIds.size - 1)
            genresOfMovie += categoriesMap[item.genreIds[i]] + " | "

        genresOfMovie = genresOfMovie.substring(0, genresOfMovie.length - 3)
        genres.text = genresOfMovie

        rating.rating = (item.voteAverage!! / 10f) * 5
        overview.text = item.overview
        Picasso.with(this)
                .load("${resources.getString(R.string.image_url_780)}${item.backdropPath}")
                .placeholder(R.drawable.poster_placeholder)
                .into(backdrop)
        Picasso.with(this)
                .load("${resources.getString(R.string.image_url_500)}${item.posterPath}")
                .placeholder(R.drawable.poster_placeholder)
                .into(poster)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val collapsingToolbarLayout: CollapsingToolbarLayout = findViewById(R.id.toolbar_layout_details) as CollapsingToolbarLayout
        app_bar_details.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1)
                    scrollRange = appBarLayout.totalScrollRange

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

    private fun getSeasons(item: Show) {
        seasonsLayout.visibility = View.VISIBLE
        Requests.getSeasons(this, item.id, listener)
    }

    fun responseListener(response: JSONObject?) {
        loadingSeasons.visibility = View.GONE
        val results = response?.getString("seasons")
        Log.i("Results", results.toString())
        if (results != null) {
            val gson = Gson()
            val items: ArrayList<Show.Seasons> = gson.fromJson(results, object : TypeToken<ArrayList<Show.Seasons>>() {}.type)
            for (item in items)
                inflateSeason(item)
        }
    }

    fun inflateSeason(item: Show.Seasons) {
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.seasons_item, seasonsLayout, false)
        (view.findViewById(R.id.seasonNumber) as TextView).text = item.seasonNumber.toString()
        (view.findViewById(R.id.episodeCount) as TextView).text = item.episodeCount.toString()
        (view.findViewById(R.id.airDate) as TextView).text = item.airDate
        Picasso.with(this)
                .load("${resources.getString(R.string.image_url_500)}${item.posterPath}")
                .placeholder(R.drawable.poster_placeholder)
                .into(view.findViewById(R.id.seasonImage) as ImageView)
        seasonsLayout.addView(view)
    }

}
