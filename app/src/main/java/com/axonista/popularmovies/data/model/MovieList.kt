package com.axonista.popularmovies.data.model;

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * model part of of MVVM architechture
 */
@Keep
data class MovieList(

    @SerializedName("page")
    var page: Int,

    @SerializedName("total_results")
    var totalResults:  Int,

    @SerializedName("total_pages")
    var totalPages: Int,

    @SerializedName("results")
    var results: ArrayList<Movie?>

) {
    data class Movie(
        @SerializedName("popularity")
        var popularity: Double,

        @SerializedName("vote_count")
        var voteCount: Int,

        @SerializedName("video")
        var video: Boolean,

        @SerializedName("poster_path")
        var posterPath: String,

        @SerializedName("id")
        var id: Int,

        @SerializedName("adult")
        var adult: Boolean,

        @SerializedName("vote_average")
        var voteAverage: Double,

        @SerializedName("release_date")
        var releaseDate: String,

        @SerializedName("title")
        var title: String,

        @SerializedName("backdrop_path")
        var backdropPath: String
    )
}
