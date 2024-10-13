package com.example.cineredux_v2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WatchlistAdapter(
    private val movieList: List<MovieSearch>,
    private val onDeleteClick: (MovieSearch) -> Unit  // Callback for delete action
) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.movie_title)
        var overviewTextView: TextView = itemView.findViewById(R.id.movie_overview)
        var posterImageView: ImageView = itemView.findViewById(R.id.movie_poster)
        var tomatometerTextView: TextView = itemView.findViewById(R.id.movie_tomatometer)
        //var yearTextView: TextView = itemView.findViewById(R.id.movie_year)
        //var trailerTextView: TextView = itemView.findViewById(R.id.movie_trailer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_watchlist, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]

        // Log all movie data
        Log.d("WatchlistAdapter", "Binding movie at position $position")
        Log.d("WatchlistAdapter", "Title: ${movie.title ?: "No Title"}")
        Log.d("WatchlistAdapter", "Overview: ${movie.overview ?: "No Overview"}")
        Log.d("WatchlistAdapter", "Tomatometer: ${movie.tomatometer ?: "N/A"}")

        // Set movie data to views
        holder.titleTextView.text = movie.title ?: "No Title"
        holder.overviewTextView.text = movie.overview ?: "No Overview"
        holder.tomatometerTextView.text = "Tomatometer: ${movie.tomatometer ?: "N/A"}"

        // Load the movie poster using Glide
        Glide.with(holder.posterImageView.context)
            .load(movie.poster)
            .placeholder(R.drawable.baseline_broken_image_24)  // Placeholder when loading
            .error(R.drawable.baseline_error_24)  // Error image if load fails
            .into(holder.posterImageView)

        // Set long-click listener to show delete confirmation
        holder.itemView.setOnLongClickListener {
            onDeleteClick(movie)  // Trigger the delete action
            true  // Indicate that the long-click was handled
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}

