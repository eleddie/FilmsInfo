package com.proyectosyntax.codingchallenge.utils

object CurrentState {
    val TYPE_TOP_RATED = "top_rated"
    val TYPE_UPCOMING = "upcoming"
    val TYPE_POPULAR = "popular"

    var categories: HashMap<Int, String> = HashMap()
    var search: String? = null

    object Movie {
        var page = 1
        var type: String = TYPE_POPULAR
    }

    object Show {
        var page = 1
        var type: String = TYPE_POPULAR
    }

    fun getCategoriesString(): String {
        var categoriesString = "["
        val categoriesList = categories.toList()
        for (it in categoriesList)
            categoriesString += it.first.toString() + "],["
        return categoriesString.substring(0, categoriesString.length - 1) + "]"
    }
}