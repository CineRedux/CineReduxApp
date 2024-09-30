package com.example.cineredux_v2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cineredux_v2.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetails : Fragment() {

    private lateinit var moviePoster: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var runtimeTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var ratingImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        moviePoster = view.findViewById(R.id.movie_poster)
        titleTextView = view.findViewById(R.id.title_text_view)
        runtimeTextView = view.findViewById(R.id.runtime_text_view)
        yearTextView = view.findViewById(R.id.year_text_view)
        ratingTextView = view.findViewById(R.id.rating_textview)
        ratingImageView = view.findViewById(R.id.rating_imageview)
        val movieId = arguments?.getInt("movieId") ?: 0
        getMovieDetails(movieId)

        return view
    }

    private fun setMovieDetails(title: String, runtime: String, year: String, posterUrl: String, rating: String,
                                istomatometer: Boolean) {
        if (istomatometer){
            ratingImageView.setImageResource(R.drawable.rotten_tomatoes)
        }else{
            ratingImageView.setImageResource(R.drawable.gold_star)
        }
        titleTextView.text = title
        runtimeTextView.text = runtime
        yearTextView.text = year
        ratingTextView.text = rating
        Glide.with(this).load(posterUrl).into(moviePoster)
        Log.d("MovieInfoFragment", istomatometer.toString())
    }
    fun convertMinutesToHoursMinutes(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h $remainingMinutes"
    }

        private fun getMovieDetails(movieId: Int){
        val movieApi = RetrofitClient.instance
        val call = movieApi.getMovieDetails(movieId, "4891cc07-b321-474d-8bb2-c5b1717d920d")
        call.enqueue(object : Callback<MovieInfo> {
            override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                if (response.isSuccessful) {
                    val movie = response.body()
                    movie?.let {
                        val title = it.title ?: "N/A"
                        val runtime = "Runtime: ${convertMinutesToHoursMinutes(it.runtime)}"
                        val year = "Release year: ${it.year ?: "N/A"}"
                        val rating = it.tomatometer ?: it.tmdbScore
                        val isTomatometer = it.tomatometer != null
                        val posterUrl = it.poster ?: ""

                        setMovieDetails(title, runtime, year, posterUrl, rating, isTomatometer)
                    }
                } else {
                    println("Request failed with status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                println("Request failed: ${t.message}")
            }
        })
    }
}
