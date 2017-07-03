package com.proyectosyntax.codingchallenge.activities

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AlertDialog
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
import java.util.*


class SplashScreen : AwesomeSplash() {

    var categoriesMap: HashMap<Int, String> = HashMap()
    var doneAnimation = false
    var doneLoading = false
    var counterCalled = 0

    lateinit var dialog: AlertDialog

    lateinit var preferences: SharedPreferences
    var failedToLoadCategories: Boolean = false

    val language: String = if (Locale.getDefault().isO3Language == "spa") "es-US" else "en-US"

    override fun initSplash(configSplash: ConfigSplash) {
        dialog = AlertDialog.Builder(this)
                .setMessage(R.string.no_internet)
                .setPositiveButton(R.string.retry, { _, _ -> getCategoriesFromServer() })
                .setNegativeButton(R.string.exit, { _, _ -> finish() })
                .create()

        preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val categoriesSaved = preferences.getString("categories", null)
        val categoriesLanguage = preferences.getString("categories-lang", null)

        if (categoriesSaved == null || categoriesLanguage != language)
            getCategoriesFromServer()
        else {
            categoriesMap = ObjectSerializer.deserialize(categoriesSaved) as HashMap<Int, String>
            doneLoading = true
        }

        configSplash.backgroundColor = R.color.colorPrimary
        configSplash.animCircularRevealDuration = 1000
        configSplash.revealFlagX = Flags.REVEAL_RIGHT
        configSplash.revealFlagY = Flags.REVEAL_BOTTOM

        configSplash.logoSplash = R.mipmap.ic_launcher
        configSplash.animLogoSplashDuration = 1000
        configSplash.animLogoSplashTechnique = Techniques.FadeIn

        configSplash.titleSplash = "Eduardo Sánchez G"
        configSplash.titleTextSize = 20f
    }

    override fun animationsFinished() {
        if (failedToLoadCategories) {
            showDialog()
            return
        }
        if (doneLoading) {
            goToMain()
        } else {
            doneAnimation = true
        }
    }

    fun getCategoriesFromServer() {

        val queue: RequestQueue = Volley.newRequestQueue(this)
        val urlMovies = "${resources.getString(R.string.api_url)}genre/movie/list?api_key=${resources.getString(R.string.api_key)}&language=$language"
        val urlShows = "${resources.getString(R.string.api_url)}genre/tv/list?api_key=${resources.getString(R.string.api_key)}&language=$language"
        val successListener = Response.Listener<JSONObject> { response -> connectionEstablished(response) }
        val errorListener = Response.ErrorListener { error -> handleError(error) }
        val moviesRequest = JsonObjectRequest(Request.Method.GET, urlMovies, null, successListener, errorListener)
        val showsRequest = JsonObjectRequest(Request.Method.GET, urlShows, null, successListener, errorListener)
        queue.add(moviesRequest)
        queue.add(showsRequest)
    }


    fun showDialog() {
        dialog.show()
    }

    fun handleError(error: VolleyError?) {
        Log.e("Error", error.toString())
        if (!failedToLoadCategories) {
            failedToLoadCategories = true
        } else {
            showDialog()
        }
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
                goToMain()
            } else {
                doneLoading = true
            }
        }

        if (counterCalled == 2) {
            val editor = preferences.edit()
            editor.putString("categories", ObjectSerializer.serialize(categoriesMap))
            editor.putString("categories-lang", language)
            editor.apply()
        }

        if (failedToLoadCategories) {
            goToMain()
        }
    }

    fun goToMain() {
        val i = android.content.Intent(this, MainActivity::class.java)
        i.putExtra("categories", categoriesMap)
        startActivity(i)
    }

}
