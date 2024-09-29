package com.example.cineredux_v2
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.android.parcel.Parcelize

data class TrendingMoviesResponse(
    @SerializedName("TopMovies") val topMovies: List<Movie>
)

@kotlinx.parcelize.Parcelize
data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster") val poster: String,
    @SerializedName("title") val title: String,
    @SerializedName("year") val year : String,
    @SerializedName("tomatometer") val tomatometer: String? = null,
    @SerializedName("trailer") val trailer: String? = null
) : Parcelable


