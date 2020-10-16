package com.axonista.popularmovies.data.network;


import com.axonista.popularmovies.data.model.MovieDetail
import com.axonista.popularmovies.data.model.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * important API class
 */
interface ApiInterface {

    @GET("popular")
    suspend fun getMovieList(
        @Query(value = "api_key") apiKey: String, @Query(value = "language") language: String, @Query(
            value = "page") pageIndex: Int
    ): Response<MovieList>

    @GET("{id}")
    suspend fun getMovieDetailData(@Path("id")  id:  Int, @Query(value = "api_key") apiKey: String, @Query(value = "language") language: String): Response<MovieDetail>

}