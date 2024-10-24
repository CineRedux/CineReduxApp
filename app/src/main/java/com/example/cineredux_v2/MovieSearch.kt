package com.example.cineredux_v2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize




@Parcelize
data class MovieSearch(
    @SerializedName("id") val id: Int,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster") val poster: String,
    @SerializedName("title") val title: String,
    @SerializedName("tmdbScore") val tomatometer: String?,
    @SerializedName("trailer") val trailer: String? = null,
    @SerializedName("year") val year: String  // Changed from Int to String
) : Parcelable

