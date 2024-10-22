package com.example.cineredux_v2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
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
    private lateinit var overviewTextView: TextView
    private lateinit var watchlistImageView: ImageView
    private lateinit var playImageView: ImageView

    // Poster and trailer URL properties
    private var posterUrl: String = ""
    private var trailerUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        initViews(view)
        val movieId = arguments?.getInt("movieId") ?: 0
        getMovieDetails(movieId)

        watchlistImageView.setOnClickListener { addToWatchlist(movieId) }
        playImageView.setOnClickListener { playTrailer(trailerUrl) }

        return view
    }

    private fun initViews(view: View) {
        moviePoster = view.findViewById(R.id.movie_poster)
        titleTextView = view.findViewById(R.id.title_text_view)
        runtimeTextView = view.findViewById(R.id.runtime_text_view)
        yearTextView = view.findViewById(R.id.year_text_view)
        ratingTextView = view.findViewById(R.id.rating_textview)
        ratingImageView = view.findViewById(R.id.rating_imageview)
        overviewTextView = view.findViewById(R.id.movie_overview)
        watchlistImageView = view.findViewById(R.id.watchlist_imageview)
        playImageView = view.findViewById(R.id.play_imageview)
    }

    private fun setMovieDetails(movie: MovieInfo) {
        val runtime = "Runtime: ${convertMinutesToHoursMinutes(movie.runtime)}"
        val year = "Release year: ${movie.year ?: "N/A"}"
        val rating = movie.tomatometer ?: movie.tmdbScore
        val isTomatometer = movie.tomatometer != null

        titleTextView.text = movie.title ?: "N/A"
        runtimeTextView.text = runtime
        yearTextView.text = year
        ratingTextView.text = rating
        overviewTextView.text = movie.overview

        ratingImageView.setImageResource(
            if (isTomatometer) R.drawable.rotten_tomatoes else R.drawable.gold_star
        )

        posterUrl = movie.poster.orEmpty()
        trailerUrl = movie.trailer.orEmpty()

        Glide.with(this).load(posterUrl).into(moviePoster)
    }

    private fun getMovieDetails(movieId: Int) {
        val movieApi = RetrofitClient.instance
        movieApi.getMovieDetails(movieId, "4891cc07-b321-474d-8bb2-c5b1717d920d")
            .enqueue(object : Callback<MovieInfo> {
                override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                    if (response.isSuccessful) {
                        response.body()?.let { setMovieDetails(it) }
                    } else {
                        Log.e("MovieDetails", "Failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                    Log.e("MovieDetails", "Request failed: ${t.message}")
                }
            })
    }

    private fun convertMinutesToHoursMinutes(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h $remainingMinutes"
    }

    private fun addToWatchlist(movieId: Int) {
        val movie = Movie(
            id = movieId,
            title = titleTextView.text.toString(),
            overview = overviewTextView.text.toString(),
            poster = posterUrl,
            tomatometer = ratingTextView.text.toString(),
            trailer = trailerUrl,
            year = yearTextView.text.toString().split(": ")[1]
        )

        val dbHelper = WatchlistDatabaseHelper(requireContext())
        try {
            val result = dbHelper.addMovie(movie.toMovieSearch())
            if (result) {
                showToast("${movie.title} added to watchlist")
            } else {
                showToast("Failed to add ${movie.title}")
            }
        } catch (e: Exception) {
            Log.e("MovieDetails", "Error: ${e.message}", e)
        }
    }

    private fun Movie.toMovieSearch() = MovieSearch(
        id = id,
        title = title,
        overview = overview,
        poster = poster,
        tomatometer = tomatometer,
        trailer = trailer,
        year = year
    )

    private fun playTrailer(url: String?) {
        if (url.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No available trailer", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
