package com.axonista.popularmovies.data.repositories;

import com.axonista.popularmovies.data.model.MovieList
import com.axonista.popularmovies.data.network.ApiInterface
import com.axonista.popularmovies.data.network.SafeApiRequest

import javax.inject.Inject
import javax.inject.Singleton


/**
 * repository for homeviewmodel
 * accessing all the l
 */

@Singleton
class MovieListRepository
@Inject constructor(
    private val api: ApiInterface
) : SafeApiRequest() {
    suspend fun getMovies(
        apiKey: String,
        language: String,
        pageIndex: Int
    ): MovieList {

        return apiRequest { api.getMovieList(apiKey, language, pageIndex) }
    }


}