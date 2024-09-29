package com.example.cineredux_v2

import android.os.Bundle
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

        val movieId = arguments?.getInt("movieId") ?: 0
        getMovieDetails(movieId)

        return view
    }

    private fun setMovieDetails(title: String, runtime: String, year: String, posterUrl: String) {
        titleTextView.text = title
        runtimeTextView.text = runtime
        yearTextView.text = year
        Glide.with(this).load(posterUrl).into(moviePoster)
    }

    private fun getMovieDetails(movieId: Int){
        val movieApi = RetrofitClient.instance
        val call = movieApi.getMovieDetails(movieId, "4891cc07-b321-474d-8bb2-c5b1717d920d")
        call.enqueue(object : Callback<MovieInfo> {
            override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                if (response.isSuccessful) {
                    val movie = response.body()
                    movie?.let {
                        // Extract data from the response and pass it to setMovieDetails
                        val title = it.title ?: "N/A"
                        val runtime = "Runtime: ${it.runtime ?: "N/A"} min"
                        val year = "Release year: ${it.year ?: "N/A"}"
                        val posterUrl = it.poster ?: ""

                        setMovieDetails(title, runtime, year, posterUrl)
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
