package com.example.cineredux_v2

data class MovieInfo(
    val title : String,
    val genres : Array<String>,
    val overview : String,
    val runtime : Int,
    val year : Int,
    val trailer : String,
    val tomatometer : String,
    val tmdbScore : String,
    val poster : String,
)
