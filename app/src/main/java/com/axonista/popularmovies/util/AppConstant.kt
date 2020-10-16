package com.axonista.popularmovies.util

object AppConstant {
    const val API_KEY = "ce2094907e30b98affddca45f64172d0"
    const val MOVIE_ID = "id"
    const val MOVIE_TITLE = "title"
    const val INTENT_POSTER = "poster"
    const val LANGUAGE = "en-US"
    const val IMAGE_URL  = "https://image.tmdb.org/t/p/w500"
    fun getImageUrl(path: String): String{
        return (IMAGE_URL + path)
    }


}