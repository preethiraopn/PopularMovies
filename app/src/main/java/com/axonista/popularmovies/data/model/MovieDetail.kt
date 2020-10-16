package com.axonista.popularmovies.data.model;

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * model part of MVVM
 */
@Keep
data class MovieDetail(
    @SerializedName("release_date")
    var releaseDate: String?,

    @SerializedName("overview")
    var overview: String?,

    @SerializedName("genres")
    var genres: ArrayList<Genre>?,

    @SerializedName("runtime")
    var runtime: Int,

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("title")
    var title: String? = null

) {
    data class Genre(
        @SerializedName("id")
        var id: Int,
        @SerializedName("name")
        var name: String
    )
}

