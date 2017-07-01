package com.proyectosyntax.codingchallenge.activities

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.daimajia.androidanimations.library.Techniques
import com.proyectosyntax.codingchallenge.R
import com.proyectosyntax.codingchallenge.utils.ObjectSerializer
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash
import org.json.JSONObject


class SplashScreen : AwesomeSplash() {

    var categoriesMap: HashMap<Int, String> = HashMap()
    var doneAnimation = false
    var doneLoading = false
    var counterCalled = 0

    lateinit var preferences: SharedPreferences

    override fun initSplash(configSplash: ConfigSplash) {

        preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val categoriesSaved = preferences.getString("categories", null)
        if (categoriesSaved == null)
            getCategoriesFromServer()
        else {
            categoriesMap = ObjectSerializer.deserialize(categoriesSaved) as HashMap<Int, String>
            doneLoading = true
        }

        //Customize Circular Reveal
        configSplash.backgroundColor = R.color.colorPrimary
        configSplash.animCircularRevealDuration = 1000
        configSplash.revealFlagX = Flags.REVEAL_RIGHT
        configSplash.revealFlagY = Flags.REVEAL_BOTTOM

        //Customize Logo
        configSplash.logoSplash = R.mipmap.ic_launcher
        configSplash.animLogoSplashDuration = 1000
        configSplash.animLogoSplashTechnique = Techniques.FadeIn

        configSplash.titleSplash = "grability"


    }

    override fun animationsFinished() {
        if (doneLoading) {
            val i = android.content.Intent(this, MainActivity::class.java)
            i.putExtra("categories", categoriesMap)
            startActivity(i)
        } else {
            doneAnimation = true
        }
    }

    fun getCategoriesFromServer() {
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
        counterCalled++
        val genresResponse = response?.getJSONArray("genres")

        if (genresResponse != null) {
            for (i in 0..genresResponse.length() - 1) {
                val current = genresResponse[i] as JSONObject
                categoriesMap.put(current.getInt("id"), current.getString("name"))
            }
            if (doneAnimation) {
                val i = android.content.Intent(this, MainActivity::class.java)
                i.putExtra("categories", categoriesMap)
                startActivity(i)
            } else {
                doneLoading = true
            }
        }

        if (counterCalled == 2) {
            preferences.edit().putString("categories", ObjectSerializer.serialize(categoriesMap)).apply()
        }

    }

}
