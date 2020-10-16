package com.axonista.popularmovies.data.repositories;

import com.axonista.popularmovies.data.model.MovieDetail
import com.axonista.popularmovies.data.network.ApiInterface
import com.axonista.popularmovies.data.network.SafeApiRequest
import com.axonista.popularmovies.util.AppConstant
import javax.inject.Inject
import javax.inject.Singleton


/**
 * repository class
 */
@Singleton
class MovieDetailRepository
@Inject constructor(private val api: ApiInterface) : SafeApiRequest() {
    suspend fun getMovieDetail(
        movieId: Int,
        apiKey: String): MovieDetail {
        return apiRequest { api.getMovieDetailData(movieId, apiKey,AppConstant.LANGUAGE) }
    }
}