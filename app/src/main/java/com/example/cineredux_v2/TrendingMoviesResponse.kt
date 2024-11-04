package com.example.cineredux_v2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrendingMoviesResponse(
    @SerializedName("TopMovies") val topMovies: List<Movie>
) : Parcelable

@Parcelize
data class MovieSearchResponse(
    @SerializedName("SimilarMovies") val results: List<Movie>
) : Parcelable

@Parcelize
data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster") val poster: String,
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: String,
    @SerializedName("tomatometer") val tomatometer: String? = null,
    @SerializedName("trailer") val trailer: String? = null
) : Parcelable

fun MovieSearch.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster = this.poster,
        tomatometer = this.tomatometer,
        trailer = this.trailer,
        year = this.year
    )
}
