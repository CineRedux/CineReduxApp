

/**
class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var movieSearchAdapter: MovieSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: Initializing views")
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize views
        searchEditText = view.findViewById(R.id.edit_search)
        searchButton = view.findViewById(R.id.btn_search)
        recyclerView = view.findViewById(R.id.recycler_view_search_results)
        progressBar = view.findViewById(R.id.progress_bar)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieSearchAdapter = MovieSearchAdapter(listOf()) { movie ->
            Log.d(TAG, "Movie poster clicked: ${movie.title}")
            navigateToWatchlistFragment(movie) // Pass the selected movie
        }

        recyclerView.adapter = movieSearchAdapter

        // Set button click listener to trigger search
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                Log.d(TAG, "Search query entered: $query")
                searchMovies(query)
            } else {
                Log.d(TAG, "Search query is empty")
            }
        }

        return view
    }

    private fun searchMovies(query: String) {
        val apiKey = "4891cc07-b321-474d-8bb2-c5b1717d920d"
        val apiService = RetrofitClient.instance
        val call = apiService.searchMovies(query, apiKey)
        Log.d(TAG, "searchMovies: Request URL = ${call.request().url}")

        progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<MovieSearchResponse2> {
            override fun onResponse(
                call: Call<MovieSearchResponse2>,
                response: Response<MovieSearchResponse2>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Successful response received")
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: Response body = ${responseBody?.let { "results size: ${it.results?.size}" } ?: "null"}")
                    val movies = responseBody?.results ?: emptyList()
                    if (movies.isNotEmpty()) {
                        Log.d(TAG, "onResponse: Number of movies received: ${movies.size}")
                    } else {
                        Log.d(TAG, "onResponse: No movies found in the response")
                    }
                    movieSearchAdapter.updateMovies(movies)
                } else {
                    Log.d(TAG, "onResponse: Response was not successful")
                    movieSearchAdapter.updateMovies(emptyList()) // Handle error response
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse2>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e(TAG, "onFailure: Error during API call", t)
                movieSearchAdapter.updateMovies(emptyList()) // Handle failure
            }
        })

    }
    private fun navigateToWatchlistFragment(movie: Movie) {
        val watchlistFragment = WatchlistFragment().apply {
            arguments = Bundle().apply {
                putParcelable("movie", movie) // Assuming Movie implements Parcelable
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, watchlistFragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}
**/

//this one works
/**package com.example.cineredux_v2

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var movieSearchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val dbHelper = WatchlistDatabaseHelper(requireContext())

        // Initialize views
        searchEditText = view.findViewById(R.id.edit_search)
        searchButton = view.findViewById(R.id.btn_search)
        recyclerView = view.findViewById(R.id.recycler_view_search_results)
        progressBar = view.findViewById(R.id.progress_bar)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieSearchAdapter = SearchAdapter(listOf(),
            onMovieClick = { movie ->
                Log.d(TAG, "Movie poster clicked: ${movie.title}")
                navigateToWatchlistFragment(movie)
            },
            onMovieLongClick = { movie ->
                showAddToWatchlistDialog(movie)
            }
        )
        recyclerView.adapter = movieSearchAdapter

        // Set button click listener to trigger search
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                Log.d(TAG, "Search query entered: $query")
                searchMovies(query)
            } else {
                Log.d(TAG, "Search query is empty")
            }
        }

        return view
    }

    private fun searchMovies(query: String) {
        val apiKey = "4891cc07-b321-474d-8bb2-c5b1717d920d"
        val apiService = RetrofitClient.instance
        val call = apiService.searchMovies(query, apiKey)
        Log.d(TAG, "searchMovies: Request URL = ${call.request().url}")

        progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<MovieSearchResponse2> {
            override fun onResponse(
                call: Call<MovieSearchResponse2>,
                response: Response<MovieSearchResponse2>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Successful response received")
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: Response body = ${responseBody?.let { "results size: ${it.results?.size}" } ?: "null"}")
                    val movies = responseBody?.results ?: emptyList()
                    if (movies.isNotEmpty()) {
                        Log.d(TAG, "onResponse: Number of movies received: ${movies.size}")
                    } else {
                        Log.d(TAG, "onResponse: No movies found in the response")
                    }
                    movieSearchAdapter.updateMovies(movies)
                } else {
                    Log.d(TAG, "onResponse: Response was not successful")
                    movieSearchAdapter.updateMovies(emptyList()) // Handle error response
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse2>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e(TAG, "onFailure: Error during API call", t)
                movieSearchAdapter.updateMovies(emptyList()) // Handle failure
            }
        })
    }

    private fun showAddToWatchlistDialog(movie: Movie) {
        // Show a dialog to confirm adding the movie to the watchlist
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add to Watchlist")
        builder.setMessage("Do you want to add ${movie.title} to your watchlist?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            addToWatchlist(movie)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun addToWatchlist(movie: Movie) {
        // Add the movie to the database or watchlist
        val dbHelper = WatchlistDatabaseHelper(requireContext())
        dbHelper.addMovie(movie)  // Assuming you have a method in dbHelper to add a movie

        Toast.makeText(requireContext(), "${movie.title} added to watchlist", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToWatchlistFragment(movie: Movie) {
        val fragment = WatchlistFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}
**/

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineredux_v2.Movie
import com.example.cineredux_v2.MovieSearchResponse2
import com.example.cineredux_v2.R
import com.example.cineredux_v2.RetrofitClient
import com.example.cineredux_v2.SearchAdapter
import com.example.cineredux_v2.WatchlistDatabaseHelper
import com.example.cineredux_v2.WatchlistAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var adapter: SearchAdapter
    private val movies = listOf<Movie>() // Assume you fetch your movie list here
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize views
        searchEditText = view.findViewById(R.id.edit_search)
        searchButton = view.findViewById(R.id.btn_search)
        recyclerView = view.findViewById(R.id.recycler_view_search_results)
        progressBar = view.findViewById(R.id.progress_bar)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(movies,
            onMovieClick = { movie ->
                Log.d(TAG, "Movie poster clicked: ${movie.title}")
                navigateToWatchlistFragment(movie)
            },
            onMovieLongClick = { movie ->
                showAddToWatchlistDialog(movie)
            }
        )
        recyclerView.adapter = adapter

        // Set button click listener to trigger search
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                Log.d(TAG, "Search query entered: $query")
                searchMovies(query)
            } else {
                Log.d(TAG, "Search query is empty")
            }
        }

        return view
    }

    private fun searchMovies(query: String) {
        val apiKey = "4891cc07-b321-474d-8bb2-c5b1717d920d"
        val apiService = RetrofitClient.instance
        val call = apiService.searchMovies(query, apiKey)
        Log.d(TAG, "searchMovies: Request URL = ${call.request().url}")

        progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<MovieSearchResponse2> {
            override fun onResponse(
                call: Call<MovieSearchResponse2>,
                response: Response<MovieSearchResponse2>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Successful response received")
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: Response body = ${responseBody?.let { "results size: ${it.results?.size}" } ?: "null"}")
                    val movies = responseBody?.results ?: emptyList()
                    if (movies.isNotEmpty()) {
                        Log.d(TAG, "onResponse: Number of movies received: ${movies.size}")
                    } else {
                        Log.d(TAG, "onResponse: No movies found in the response")
                    }
                    adapter.updateMovies(movies)
                } else {
                    Log.d(TAG, "onResponse: Response was not successful")
                    adapter.updateMovies(emptyList()) // Handle error response
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse2>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e(TAG, "onFailure: Error during API call", t)
                adapter.updateMovies(emptyList()) // Handle failure
            }
        })
    }

    private fun showAddToWatchlistDialog(movie: Movie) {
        Log.d(TAG, "Showing add to watchlist dialog for movie: ${movie.title}")
        // Show a dialog to confirm adding the movie to the watchlist
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add to Watchlist")
        builder.setMessage("Do you want to add ${movie.title} to your watchlist?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            addToWatchlist(movie)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun addToWatchlist(movie: Movie) {
        Log.d(TAG, "Adding movie to watchlist: ${movie.title}")
        // Add the movie to the database or watchlist
        val dbHelper = WatchlistDatabaseHelper(requireContext())
        try {
            dbHelper.addMovie(movie)
            Toast.makeText(requireContext(), "${movie.title} added to watchlist", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding movie to watchlist: ${e.message}", e)
        }
    }

    private fun navigateToWatchlistFragment(movie: Movie) {
        Log.d(TAG, "Navigating to WatchlistFragment")
        val fragment = WatchlistFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}
