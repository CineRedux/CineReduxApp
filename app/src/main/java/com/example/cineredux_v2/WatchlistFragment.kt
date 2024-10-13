package com.example.cineredux_v2

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
        adapter = WatchlistAdapter(movieArrayList)
        recyclerView.adapter = adapter

        return view
    }
}
