package com.example.cineredux_v2

import android.app.AlertDialog
import android.content.ContentValues.TAG
import com.example.cineredux_v2.Movie
import com.example.cineredux_v2.TrendingMoviesResponse
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        // Observe cached movies
        viewModel.cachedMovies.observe(viewLifecycleOwner) { cachedMovies ->
            if (cachedMovies != null && cachedMovies.isNotEmpty() && isAdded) {
                Log.d("HomeFragment", "Displaying cached movies")
                displayMovies(view, cachedMovies)
            }
        }

        // Fetch trending movies from API if no cache available
        if (viewModel.cachedMovies.value.isNullOrEmpty()) {
            Log.d("HomeFragment", "No Cached movies, fetching from api")
            fetchMoviesFromAPI(view)
        }
    }

    private fun fetchMoviesFromAPI(view: View) {
        val apiService = RetrofitClient.instance
        val call = apiService.getTrendingMovies("4891cc07-b321-474d-8bb2-c5b1717d920d")

        call.enqueue(object : Callback<TrendingMoviesResponse> {
            override fun onResponse(call: Call<TrendingMoviesResponse>, response: Response<TrendingMoviesResponse>) {
                if (!isAdded) return  // Check if fragment is attached before proceeding
                
                if (response.isSuccessful) {
                    val movies = response.body()?.topMovies
                    Log.d("HomeFragment", "Movies fetched: $movies")
                    if (movies != null) {
                        // Cache movies in ViewModel
                        viewModel.cachedMovies.value = movies
                        if (isAdded) {  // Check again before displaying
                            displayMovies(view, movies)
                        }
                    }
                } else {
                    if (response.code() == 502) {
                        Log.d("HomeFragment", "Received 502 error. Retrying...")
                        call.clone().enqueue(this)
                    } else {
                        Log.e("HomeFragment", "${response.code()}: ${response.errorBody()?.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<TrendingMoviesResponse>, t: Throwable) {
                if (!isAdded) return  // Check if fragment is attached
                
                Log.e("HomeFragment", "Failed to load trending movies", t)
                Toast.makeText(requireContext(), getString(R.string.loading_error), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayMovies(view: View, movies: List<Movie>?) {
        if (!isAdded) return  // Check if fragment is attached
        
        val gridLayout = view.findViewById<GridLayout>(R.id.grid_layout_movies)
        movies?.let { movieList ->
            gridLayout.removeAllViews()

            movieList.forEach { movie ->
                if (!isAdded) return@forEach  // Check if fragment is still attached in the loop
                
                val movieView = LayoutInflater.from(requireContext()).inflate(R.layout.movie_item, gridLayout, false)

                val titleTextView = movieView.findViewById<TextView>(R.id.movie_title)
                val yearTextView = movieView.findViewById<TextView>(R.id.movie_year)
                val tomatoMeterTextView = movieView.findViewById<TextView>(R.id.movie_tomatometer)
                val posterImageView = movieView.findViewById<ImageView>(R.id.movie_poster)

                // Set movie data
                titleTextView.text = movie.title
                yearTextView.text = movie.year
                tomatoMeterTextView.text = getString(R.string.tomatometer, movie.tomatometer)
                Glide.with(this).load(movie.poster).into(posterImageView)

                // Set click listeners for the movie poster
                posterImageView.setOnClickListener {
                    //navigateToWatchlistFragment()
                    navigateToMovieDetails(movie.id)
                }

                // Handle long-click event to add to watchlist
                posterImageView.setOnLongClickListener {
                    showAddToWatchlistDialog(movie)
                    true // Return true to indicate the event is handled
                }


                val layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED)
                    setMargins(8, 8, 8, 8)
                }

                movieView.layoutParams = layoutParams
                gridLayout.addView(movieView)
            }
        }
    }

    private fun showAddToWatchlistDialog(movie: Movie) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_to_watchlist))
            .setMessage(getString(R.string.add_to_watchlist_message, movie.title))
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                //addToWatchlist(movie)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val movieDetails = MovieDetails()
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        movieDetails.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, movieDetails)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
