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
    private val onDeleteClick: (MovieSearch) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.movie_title)
        var overviewTextView: TextView = itemView.findViewById(R.id.movie_overview)
        var posterImageView: ImageView = itemView.findViewById(R.id.movie_poster)
        var tomatometerTextView: TextView = itemView.findViewById(R.id.movie_tomatometer)
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

        holder.titleTextView.text = movie.title ?: "No Title"
        holder.overviewTextView.text = movie.overview ?: "No Overview"
        holder.tomatometerTextView.text = "Tomatometer: ${movie.tomatometer ?: "N/A"}"

        Glide.with(holder.posterImageView.context)
            .load(movie.poster)
            .placeholder(R.drawable.baseline_broken_image_24)  // Placeholder when loading
            .error(R.drawable.baseline_error_24)
            .into(holder.posterImageView)

        holder.itemView.setOnLongClickListener {
            onDeleteClick(movie)
            true
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}

