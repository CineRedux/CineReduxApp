package com.example.cineredux_v2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    // Endpoint to get trending movies
    @GET("trending")
    fun getTrendingMovies(@Query("api_key") apiKey: String): Call<TrendingMoviesResponse>

    // Endpoint to search for movies
    @GET("movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Call<MovieSearchResponse2>
}
