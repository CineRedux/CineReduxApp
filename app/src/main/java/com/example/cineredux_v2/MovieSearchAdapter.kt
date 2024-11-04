package com.example.cineredux_v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit,
    private val onMovieLongClick: (Movie) -> Unit
) : RecyclerView.Adapter<SearchAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val movieOverview: TextView = itemView.findViewById(R.id.movie_overview)
        private val movieRating: TextView = itemView.findViewById(R.id.movie_tomatometer) // Tomatometer
        private val movieTrailer: TextView = itemView.findViewById(R.id.movie_trailer) // Trailer

        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            movieOverview.text = movie.overview
            movieRating.text = "Rating: ${movie.tomatometer ?: "N/A"}" // Tomatometer rating
            movieTrailer.text = "Trailer: ${movie.trailer ?: "N/A"}" // Trailer URL or message

            Glide.with(itemView.context)
                .load(movie.poster)
                .into(moviePoster)

            // Handle click event
            itemView.setOnClickListener {
                onMovieClick(movie) // Call the provided onMovieClick lambda
            }

            // Handle long-click event
            itemView.setOnLongClickListener {
                onMovieLongClick(movie)
                true
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
