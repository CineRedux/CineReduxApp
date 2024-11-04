package com.example.cineredux_v2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {


    @GET("trending")
    fun getTrendingMovies(@Query("api_key") apiKey: String): Call<TrendingMoviesResponse>

    @GET("search")
    fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Call<MovieSearchResponse>

    @GET("/movie/{movieId}")
    fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieInfo>

}
