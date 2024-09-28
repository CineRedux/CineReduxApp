package com.example.cineredux_v2




import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide




class MoviesAdapter(
    private val movies: List<Movie>,  // Explicitly accept List<Movie>
    private val listener: OnMovieClickListener,
    private val onMovieLongClick: (Movie) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posterImageView: ImageView = view.findViewById(R.id.movie_poster)
        val titleTextView: TextView = view.findViewById(R.id.movie_title)
        val tomatometerTextView: TextView = view.findViewById(R.id.movie_tomatometer)
        val overviewTextView: TextView = view.findViewById(R.id.movie_overview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.titleTextView.text = movie.title
        holder.tomatometerTextView.text = "Tomatometer: ${movie.tomatometer ?: "N/A"}"
        holder.overviewTextView.text = movie.overview

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + movie.poster)
            .into(holder.posterImageView)

        holder.posterImageView.setOnClickListener {
            listener.onMoviePosterClick(movie)
        }

        holder.posterImageView.setOnLongClickListener {
            onMovieLongClick(movie)
            true // Return true to indicate the event is handled
        }

    }


    override fun getItemCount() = movies.size


}
