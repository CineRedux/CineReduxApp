package com.example.cineredux_v2



import WatchlistFragment
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//THE
/**


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
viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

viewModel.cachedMovies.observe(viewLifecycleOwner) { cachedMovies ->
if (cachedMovies != null && cachedMovies.isNotEmpty()) {
displayMovies(requireView(), cachedMovies) // Safely unwrap the view
} else {
fetchMoviesFromAPI(requireView()) // Use requireView() to ensure non-null view
}
}
}

private fun fetchMoviesFromAPI(view: View) {
val apiService = RetrofitClient.instance
val call = apiService.getTrendingMovies("4891cc07-b321-474d-8bb2-c5b1717d920d")

call.enqueue(object : Callback<TrendingMoviesResponse> {
override fun onResponse(call: Call<TrendingMoviesResponse>, response: Response<TrendingMoviesResponse>) {
if (response.isSuccessful) {
val movies = response.body()?.topMovies
Log.d("HomeFragment", "Movies fetched: $movies")
if (movies != null) {
viewModel.cachedMovies.value = movies // Cache movies in ViewModel
displayMovies(requireView(), movies) // Ensure non-null view
}
} else {
Log.e("HomeFragment", "Response not successful: ${response.errorBody()?.string()}")
}
}

override fun onFailure(call: Call<TrendingMoviesResponse>, t: Throwable) {
Log.e("HomeFragment", "Failed to load trending movies", t)
Toast.makeText(requireContext(), "Failed to load trending movies. Please try again later.", Toast.LENGTH_LONG).show()
}
})
}

private fun displayMovies(view: View, movies: List<Movie>?) {
val homeLayout = view.findViewById<ViewGroup>(R.id.home_layout)

movies?.let { movieList ->
movieList.forEach { movie ->
val movieView = LayoutInflater.from(requireContext()).inflate(R.layout.movie_item, homeLayout, false)

val titleTextView = movieView.findViewById<TextView>(R.id.movie_title)
val overviewTextView = movieView.findViewById<TextView>(R.id.movie_overview)
val posterImageView = movieView.findViewById<ImageView>(R.id.movie_poster)

titleTextView.text = movie.title
overviewTextView.text = movie.overview
Glide.with(this).load(movie.poster).into(posterImageView)

// Add click listener for poster
posterImageView.setOnClickListener {
navigateToWatchlistFragment()
}

// Handle long-click event to add to watchlist
posterImageView.setOnLongClickListener {
showAddToWatchlistDialog(movie)
true // Return true to indicate the event is handled
}

homeLayout.addView(movieView)
}
}
}

private fun showAddToWatchlistDialog(movie: Movie) {
val dialog = AlertDialog.Builder(requireContext())
dialog.setTitle("Add to Watchlist")
dialog.setMessage("Do you want to add ${movie.title} to your watchlist?")
dialog.setPositiveButton("Add") { _, _ ->
addToWatchlist(movie)
}
dialog.setNegativeButton("Cancel", null)
dialog.show()
}

private fun navigateToWatchlistFragment() {
val watchlistFragment = WatchlistFragment()
parentFragmentManager.beginTransaction()
.replace(R.id.fragment_container, watchlistFragment)
.addToBackStack(null)
.commit()
}

private fun addToWatchlist(movie: Movie) {
Log.d(TAG, "Adding movie to watchlist: ${movie.title}")

val dbHelper = WatchlistDatabaseHelper(requireContext())

Log.d("WatchlistAdapter", "ID: ${movie.id}")
Log.d("WatchlistAdapter", "Title: ${movie.title}")
Log.d("WatchlistAdapter", "Overview: ${movie.overview}")
Log.d("WatchlistAdapter", "Poster: ${movie.poster}")
Log.d("WatchlistAdapter", "TMDB Score: ${movie.tomatometer ?: "N/A"}")
Log.d("WatchlistAdapter", "Trailer: ${movie.trailer ?: "No Trailer"}")

try {
dbHelper.addMovie(movie)
Toast.makeText(requireContext(), "${movie.title} added to watchlist", Toast.LENGTH_SHORT).show()
} catch (e: Exception) {
Log.e(TAG, "Error adding movie to watchlist: ${e.message}", e)
}
}
}


 */


// WORKING CLASS
/**
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
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)




        val apiService = RetrofitClient.instance
        val call = apiService.getTrendingMovies("4891cc07-b321-474d-8bb2-c5b1717d920d")

        call.enqueue(object : Callback<TrendingMoviesResponse> {
            override fun onResponse(call: Call<TrendingMoviesResponse>, response: Response<TrendingMoviesResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.topMovies
                    Log.d("HomeFragment", "Movies fetched: $movies")
                    movies?.forEach { movie ->
                        Log.d("HomeFragment", "Tomatometer for ${movie.title}: ${movie.tomatometer}")
                    }
                    displayMovies(view, movies)
                } else {
                    Log.e("HomeFragment", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TrendingMoviesResponse>, t: Throwable) {
                Log.e("HomeFragment", "Failed to load trending movies", t)
                Toast.makeText(requireContext(), "Failed to load trending movies. Please try again later.", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.cachedMovies.observe(viewLifecycleOwner) { cachedMovies ->
            if (cachedMovies != null && cachedMovies.isNotEmpty()) {
                displayMovies(view, cachedMovies)
            } else {
                fetchMoviesFromAPI(view)
            }
        }
    }

    private fun fetchMoviesFromAPI(view: View) {
        val apiService = RetrofitClient.instance
        val call = apiService.getTrendingMovies("4891cc07-b321-474d-8bb2-c5b1717d920d")

        call.enqueue(object : Callback<TrendingMoviesResponse> {
            override fun onResponse(call: Call<TrendingMoviesResponse>, response: Response<TrendingMoviesResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.topMovies
                    Log.d("HomeFragment", "Movies fetched: $movies")
                    if (movies != null) {
                        // Cache movies in ViewModel
                        viewModel.cachedMovies.value = movies
                        displayMovies(view, movies)
                    }
                } else {
                    Log.e("HomeFragment", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TrendingMoviesResponse>, t: Throwable) {
                Log.e("HomeFragment", "Failed to load trending movies", t)
                Toast.makeText(requireContext(), "Failed to load trending movies. Please try again later.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayMovies(view: View, movies: List<Movie>?) {
        val homeLayout = view.findViewById<ViewGroup>(R.id.home_layout)

        movies?.let { movieList ->
            movieList.forEach { movie ->
                val movieView = LayoutInflater.from(requireContext()).inflate(R.layout.movie_item, homeLayout, false)

                val titleTextView = movieView.findViewById<TextView>(R.id.movie_title)
                val overviewTextView = movieView.findViewById<TextView>(R.id.movie_overview)
                val posterImageView = movieView.findViewById<ImageView>(R.id.movie_poster)

                titleTextView.text = movie.title
                overviewTextView.text = movie.overview
                Glide.with(this).load(movie.poster).into(posterImageView)

                posterImageView.setOnClickListener {
                    navigateToWatchlistFragment()
                }

                // Handle long-click event to add to watchlist
                posterImageView.setOnLongClickListener {
                    showAddToWatchlistDialog(movie)
                    true // Return true to indicate the event is handled
                }

                homeLayout.addView(movieView)
            }
        }
    }

    private fun showAddToWatchlistDialog(movie: Movie) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Add to Watchlist")
        dialog.setMessage("Do you want to add ${movie.title} to your watchlist?")
        dialog.setPositiveButton("Add") { _, _ ->
            addToWatchlist(movie)
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }


    private fun navigateToWatchlistFragment() {
        val watchlistFragment = WatchlistFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, watchlistFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun addToWatchlist(movie: Movie) {
        Log.d(TAG, "Adding movie to watchlist: ${movie.title}")

        val dbHelper = WatchlistDatabaseHelper(requireContext())

        // Debugging to ensure correct movie data before saving
        Log.d("WatchlistAdapter", "ID: ${movie.id}")
        Log.d("WatchlistAdapter", "Title: ${movie.title}")
        Log.d("WatchlistAdapter", "Overview: ${movie.overview}")
        Log.d("WatchlistAdapter", "Poster: ${movie.poster}")
        Log.d("WatchlistAdapter", "TMDB Score: ${movie.tomatometer ?: "N/A"}")
        Log.d("WatchlistAdapter", "Trailer: ${movie.trailer ?: "No Trailer"}")

        try {
            dbHelper.addMovie(movie)  // Ensure movie fields are correctly passed here
            Toast.makeText(requireContext(), "${movie.title} added to watchlist", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding movie to watchlist: ${e.message}", e)
        }
    }


}

**/

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
            if (cachedMovies != null && cachedMovies.isNotEmpty()) {
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
                if (response.isSuccessful) {
                    val movies = response.body()?.topMovies
                    Log.d("HomeFragment", "Movies fetched: $movies")
                    if (movies != null) {
                        // Cache movies in ViewModel
                        viewModel.cachedMovies.value = movies
                        displayMovies(view, movies)
                    }
                } else {
                    if (response.code() == 502) {
                        Log.d("HomeFragment", "Received 502 error. Retrying...")
                        call.clone().enqueue(this)
                    }
                    else{
                        Log.e("HomeFragment", "${response.code()}: ${response.errorBody()?.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<TrendingMoviesResponse>, t: Throwable) {
                Log.e("HomeFragment", "Failed to load trending movies", t)
                Toast.makeText(requireContext(), "Failed to load trending movies. Please try again later.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayMovies(view: View, movies: List<Movie>?) {
        val gridLayout = view.findViewById<GridLayout>(R.id.grid_layout_movies)

        movies?.let { movieList ->
            gridLayout.removeAllViews()

            movieList.forEach { movie ->
                val movieView = LayoutInflater.from(requireContext()).inflate(R.layout.movie_item, gridLayout, false)

                val titleTextView = movieView.findViewById<TextView>(R.id.movie_title)
                val yearTextView = movieView.findViewById<TextView>(R.id.movie_year)
                val tomatoMeterTextView = movieView.findViewById<TextView>(R.id.movie_tomatometer)
                val posterImageView = movieView.findViewById<ImageView>(R.id.movie_poster)

                // Set movie data
                titleTextView.text = movie.title
                yearTextView.text = movie.year
                tomatoMeterTextView.text = "Tomatometer: ${movie.tomatometer}"
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
        dialog.setTitle("Add to Watchlist")
        dialog.setMessage("Do you want to add ${movie.title} to your watchlist?")
        dialog.setPositiveButton("Add") { _, _ ->
            addToWatchlist(movie)
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }

    private fun navigateToWatchlistFragment() {
        val watchlistFragment = WatchlistFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, watchlistFragment)
            .addToBackStack(null)
            .commit()
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

    private fun addToWatchlist(movie: Movie) {
        Log.d(TAG, "Adding movie to watchlist: ${movie.title}")

        val dbHelper = WatchlistDatabaseHelper(requireContext())

        try {
            dbHelper.addMovie(movie)
            Toast.makeText(requireContext(), "${movie.title} added to watchlist", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding movie to watchlist: ${e.message}", e)
        }
    }
}
