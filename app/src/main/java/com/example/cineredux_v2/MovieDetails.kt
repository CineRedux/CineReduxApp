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
    private lateinit var watchlistImageView: ImageView // For adding to watchlist
    private lateinit var playImageView: ImageView // For playing trailer
    private lateinit var poster_url: String
    private lateinit var trailer_url: String

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
        overviewTextView = view.findViewById(R.id.movie_overview)
        watchlistImageView = view.findViewById(R.id.watchlist_imageview) // Initialize the ImageView
        playImageView = view.findViewById(R.id.play_imageview) // Initialize the play ImageView

        val movieId = arguments?.getInt("movieId") ?: 0
        getMovieDetails(movieId)

        // Set click listener for the watchlist ImageView
        watchlistImageView.setOnClickListener {
            // Call the addToWatchlist method
            val movie = Movie(
                id = movieId,
                title = titleTextView.text.toString(),
                overview = overviewTextView.text.toString(),
                poster = poster_url, // Ensure this is a valid poster URL or resource
                tomatometer = ratingTextView.text.toString(), // Use the correct value for the tomatometer
                trailer = trailer_url,
                year = yearTextView.text.toString().split(": ")[1] // Extract the year from the TextView
            )
            addToWatchlist(movie) // Call the method to add to watchlist
        }

        // Set click listener for the play ImageView
        playImageView.setOnClickListener {
            val trailerUrl = trailer_url
            playTrailer(trailerUrl) // Play the trailer
        }

        return view
    }

    private fun setMovieDetails(
        title: String, runtime: String, year: String, posterUrl: String,
        rating: String, isTomatometer: Boolean, overview: String, trailerUrl: String
    ) {
        if (isTomatometer) {
            ratingImageView.setImageResource(R.drawable.rotten_tomatoes)
        } else {
            ratingImageView.setImageResource(R.drawable.gold_star)
        }
        titleTextView.text = title
        runtimeTextView.text = runtime
        yearTextView.text = year
        ratingTextView.text = rating
        overviewTextView.text = overview

        // Load the movie poster
        Glide.with(this).load(posterUrl).into(moviePoster)
        Log.d("MovieInfoFragment", isTomatometer.toString())
    }

    fun convertMinutesToHoursMinutes(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h $remainingMinutes"
    }

    private fun getMovieDetails(movieId: Int) {
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
                        val overview = it.overview
                        val trailerUrl = it.trailer ?: "" // Assuming you have a trailer URL field in MovieInfo
                        poster_url = posterUrl
                        trailer_url = trailerUrl
                        setMovieDetails(title, runtime, year, posterUrl, rating, isTomatometer, overview, trailerUrl)
                    }
                } else {
                    Log.e("MovieDetails", "Request failed with status: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                Log.e("MovieDetails", "Request failed: ${t.message}")
            }
        })
    }

    private fun addToWatchlist(movie: Movie) {
        Log.d("MovieDetails", "Adding movie to watchlist: ${movie.title}")
        // Convert Movie to MovieSearch
        val movieSearch = MovieSearch(
            id = movie.id,
            overview = movie.overview,
            poster = movie.poster,
            title = movie.title,
            tomatometer = movie.tomatometer,
            trailer = movie.trailer, // Add the trailer URL
            year = movie.year // Assuming `year` in Movie is a String
        )

        // Add the movie to the database or watchlist
        val dbHelper = WatchlistDatabaseHelper(requireContext())
        try {
            Log.d("MovieDetails", "Preparing to insert movie: ${movieSearch.title}")
            val result = dbHelper.addMovie(movieSearch) // Ensure this method can handle MovieSearch
            if (result) {
                Log.d("MovieDetails", "${movieSearch.title} inserted successfully into the watchlist.")
                Toast.makeText(requireContext(), "${movieSearch.title} added to watchlist", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("MovieDetails", "Failed to insert ${movieSearch.title} into the watchlist.")
                Toast.makeText(requireContext(), "Failed to add ${movieSearch.title} to watchlist", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("MovieDetails", "Error adding movie to watchlist: ${e.message}", e)
        }
    }
    // Function to play the trailer using an intent
    private fun playTrailer(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
