package com.example.cineredux_v2

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
/**
class MovieSearchAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieSearchAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val movieTomatometer: TextView = itemView.findViewById(R.id.movie_tomatometer)
        private val movieOverview: TextView = itemView.findViewById(R.id.movie_overview)

        fun bind(movie: Movie) {
            Log.d(TAG, "Binding movie: ${movie.tomatometer}") // Check the value of tmdbScore

            movieTitle.text = movie.title
            movieOverview.text = movie.overview
            movieTomatometer.text = "Tomatometer: ${movie.tomatometer  ?: "N/A"}"

            Glide.with(itemView.context)
                .load(movie.poster)
                .into(moviePoster)

            itemView.setOnClickListener { onMovieClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>?) {
        movies = newMovies ?: emptyList()
        notifyDataSetChanged()
    }
}**/

class SearchAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit,
    private val onMovieLongClick: (Movie) -> Unit // New long click listener
) : RecyclerView.Adapter<SearchAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val movieOverview: TextView = itemView.findViewById(R.id.movie_overview)

        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            movieOverview.text = movie.overview

            Glide.with(itemView.context)
                .load(movie.poster)
                .into(moviePoster)

            // Handle click event
            itemView.setOnClickListener { onMovieClick(movie) }

            // Handle long-click event
            itemView.setOnLongClickListener {
                onMovieLongClick(movie)
                true // Return true to indicate the event is handled
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item_search, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>?) {
        movies = newMovies ?: emptyList()
        notifyDataSetChanged()
    }
}
