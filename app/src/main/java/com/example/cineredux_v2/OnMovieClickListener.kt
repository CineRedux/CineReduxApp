package com.example.cineredux_v2

import com.example.cineredux_v2.Movie
interface OnMovieClickListener {
    fun onMoviePosterClick(movie: Movie)
    fun onMovieLongClick(movie: Movie)
}
