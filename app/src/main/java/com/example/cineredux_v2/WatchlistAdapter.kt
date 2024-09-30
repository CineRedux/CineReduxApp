package com.example.cineredux_v2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WatchlistAdapter(private val movieList: ArrayList<Movie>) :
    RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.movie_title)
        var overviewTextView: TextView = itemView.findViewById(R.id.movie_overview)
        var posterImageView: ImageView = itemView.findViewById(R.id.movie_poster)
        var tomatometerTextView: TextView = itemView.findViewById(R.id.movie_tomatometer)
       // var trailerTextView: TextView = itemView.findViewById(R.id.movie_trailer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_watchlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]

        // Log all movie data
        Log.d("WatchlistAdapter", "Binding movie at position $position")
        Log.d("WatchlistAdapter", "Title: ${movie.title ?: "No Title"}")
        Log.d("WatchlistAdapter", "Overview: ${movie.overview ?: "No Overview"}")
        Log.d("WatchlistAdapter", "Tomatometer: ${movie.tomatometer ?: "N/A"}")
        Log.d("WatchlistAdapter", "Trailer: ${movie.trailer ?: "No Trailer"}")

        // Set movie data to views
        holder.titleTextView.text = movie.title ?: "No Title"
        holder.overviewTextView.text = movie.overview ?: "No Overview"
        holder.tomatometerTextView.text = "Tomatometer: ${movie.tomatometer ?: "N/A"}"

        // Handle possible null or default URL for the movie poster
        val posterUrl = movie.poster ?: null
        Log.d("WatchlistAdapter", "Loading poster URL: $posterUrl")

        // Load the movie poster using Glide
        Glide.with(holder.posterImageView.context)
            .load(posterUrl)
            .placeholder(R.drawable.baseline_broken_image_24)  // Placeholder when loading
            .error(R.drawable.baseline_error_24)  // Error image if load fails
            .into(holder.posterImageView)

        // Handle trailer TextView visibility and content
//        if (movie.trailer.isNullOrEmpty()) {
//            holder.trailerTextView.visibility = View.GONE
//            Log.d("WatchlistAdapter", "Trailer TextView is set to GONE")
//        } else {
//            holder.trailerTextView.visibility = View.VISIBLE
//            holder.trailerTextView.text = "Trailer: ${movie.trailer}"
//            Log.d("WatchlistAdapter", "Trailer TextView is set to VISIBLE with text: Trailer: ${movie.trailer}")
//        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}
