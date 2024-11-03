package com.example.cineredux_v2

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WatchlistFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WatchlistAdapter
    private lateinit var dbHelper: WatchlistDatabaseHelper
    private lateinit var movieArrayList: ArrayList<MovieSearch>  // Change type to MovieSearch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_watchlist, container, false)

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_watchlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize database helper
        dbHelper = WatchlistDatabaseHelper(requireContext())

        // Fetch cached movies from the database
        movieArrayList = dbHelper.getWatchlistMovies() as ArrayList<MovieSearch> // Fetch movies directly as ArrayList

        // Set up the RecyclerView with cached movies
        adapter = WatchlistAdapter(movieArrayList) { movie ->
            showDeleteConfirmationDialog(movie)  // Show dialog on long click
        }
        recyclerView.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        loadMoviesFromDatabase() // Reload movies every time the fragment is visible
    }

    private fun loadMoviesFromDatabase() {
        movieArrayList = dbHelper.getWatchlistMovies() as ArrayList<MovieSearch>
        adapter = WatchlistAdapter(movieArrayList) { movie ->
            showDeleteConfirmationDialog(movie)  // Pass the delete action callback
        }
        recyclerView.adapter = adapter
        Log.d("WatchlistFragment", "Movies loaded: ${movieArrayList.size}")
    }

    private fun deleteMovie(movie: MovieSearch) {
        val success = dbHelper.deleteMovieById(movie.id)
        if (success) {
            movieArrayList.remove(movie)  // Remove from list
            adapter.notifyDataSetChanged()  // Notify adapter of data change
            Log.d("WatchlistFragment", "${movie.title} deleted successfully.")
        } else {
            Log.e("WatchlistFragment", "Failed to delete ${movie.title}.")
        }
    }

    private fun showDeleteConfirmationDialog(movie: MovieSearch) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_from_watchlist))
            .setMessage(getString(R.string.delete_movie_message, movie.title))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                deleteMovie(movie)
            }
            .setNegativeButton(getString(R.string.no)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}

