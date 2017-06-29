package com.proyectosyntax.codingchallenge

import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash
import org.json.JSONObject


class SplashScreen : AwesomeSplash() {

    var categoriesMap: HashMap<Int, String> = HashMap()

    var doneAnmation = false
    var doneLoading = false

    override fun initSplash(configSplash: ConfigSplash) {

        getCategories()

        //Customize Circular Reveal
        configSplash.backgroundColor = R.color.colorPrimary //any color you want form colors.xml
        configSplash.animCircularRevealDuration = 1000 //int ms
        configSplash.revealFlagX = Flags.REVEAL_RIGHT  //or Flags.REVEAL_LEFT
        configSplash.revealFlagY = Flags.REVEAL_BOTTOM //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.logoSplash = R.mipmap.ic_launcher //or any other drawable
        configSplash.animLogoSplashDuration = 2000 //int ms
        configSplash.animLogoSplashTechnique = Techniques.Bounce //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
//        configSplash.pathSplash = Constants.DROID_LOGO //set path String
        configSplash.originalHeight = 400 //in relation to your svg (path) resource
        configSplash.originalWidth = 400 //in relation to your svg (path) resource
        configSplash.animPathStrokeDrawingDuration = 1000
        configSplash.pathSplashStrokeSize = 3 //I advise value be <5
        configSplash.pathSplashStrokeColor = R.color.colorAccent //any color you want form colors.xml
        configSplash.animPathFillingDuration = 3000
        configSplash.pathSplashFillColor = R.color.colorAccent //path object filling color


        //Customize Title
        configSplash.titleSplash = "Grability"
        configSplash.titleTextColor = R.color.colorAccent
        configSplash.titleTextSize = 30f //float value
        configSplash.animTitleDuration = 1000
        configSplash.animTitleTechnique = Techniques.FlipInX

    }

    override fun animationsFinished() {
        if (doneLoading) {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("categories", categoriesMap)
            startActivity(i)
        } else {
            doneAnmation = true
        }
    }

    fun getCategories() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val urlMovies = "${resources.getString(R.string.api_url)}genre/movie/list?api_key=${resources.getString(R.string.api_key)}"
        val urlShows = "${resources.getString(R.string.api_url)}genre/tv/list?api_key=${resources.getString(R.string.api_key)}"
        val successListener = Response.Listener<JSONObject> { response -> connectionEstablished(response) }
        val errorListener = Response.ErrorListener { error -> handleError(error) }
        val moviesRequest = JsonObjectRequest(Request.Method.GET, urlMovies, null, successListener, errorListener)
        val showsRequest = JsonObjectRequest(Request.Method.GET, urlShows, null, successListener, errorListener)
        queue.add(moviesRequest)
        queue.add(showsRequest)
    }

    fun handleError(error: VolleyError?) {
        Log.e("Error", error.toString())
    }

    fun connectionEstablished(response: JSONObject?) {
        val genresResponse = response?.getJSONArray("genres")

        if (genresResponse != null) {
            for (i in 0..genresResponse.length() - 1) {
                val current = genresResponse[i] as JSONObject
                if (categoriesMap.get(current.getInt("id")) == null)
                    categoriesMap.put(current.getInt("id"), current.getString("name"))
            }
            if (doneAnmation) {
                val i = Intent(this, MainActivity::class.java)
                i.putExtra("categories", categoriesMap)
                startActivity(i)
            } else {
                doneLoading = true
            }
        }

    }
}
