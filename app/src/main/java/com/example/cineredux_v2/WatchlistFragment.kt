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
    private lateinit var movieArrayList: ArrayList<MovieSearch>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_watchlist, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_watchlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dbHelper = WatchlistDatabaseHelper(requireContext())

        movieArrayList = dbHelper.getWatchlistMovies() as ArrayList<MovieSearch>

        adapter = WatchlistAdapter(movieArrayList) { movie ->
            showDeleteConfirmationDialog(movie)
        }
        recyclerView.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        loadMoviesFromDatabase()
    }

    private fun loadMoviesFromDatabase() {
        movieArrayList = dbHelper.getWatchlistMovies() as ArrayList<MovieSearch>
        adapter = WatchlistAdapter(movieArrayList) { movie ->
            showDeleteConfirmationDialog(movie)
        }
        recyclerView.adapter = adapter
        Log.d("WatchlistFragment", "Movies loaded: ${movieArrayList.size}")
    }

    private fun deleteMovie(movie: MovieSearch) {
        val success = dbHelper.deleteMovieById(movie.id)
        if (success) {
            movieArrayList.remove(movie)
            adapter.notifyDataSetChanged()
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

