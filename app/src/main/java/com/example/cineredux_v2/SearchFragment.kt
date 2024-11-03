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
import com.example.cineredux_v2.MovieDetails
import com.example.cineredux_v2.MovieSearch
import com.example.cineredux_v2.MovieSearchResponse
import com.example.cineredux_v2.R
import com.example.cineredux_v2.RetrofitClient
import com.example.cineredux_v2.SearchAdapter
import com.example.cineredux_v2.WatchlistDatabaseHelper
import com.example.cineredux_v2.WatchlistFragment
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
                navigateToMovieDetails(movie.id) // Update this line to navigate to movie details
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

        searchEditText.hint = getString(R.string.search_hint)
        searchButton.text = getString(R.string.search_button)

        return view
    }

    private fun searchMovies(query: String) {
        val apiKey = "4891cc07-b321-474d-8bb2-c5b1717d920d"
        val apiService = RetrofitClient.instance
        val call = apiService.searchMovies(query, apiKey)
        Log.d(TAG, "searchMovies: Request URL = ${call.request().url}")

        progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<MovieSearchResponse> {
            override fun onResponse(
                call: Call<MovieSearchResponse>,
                response: Response<MovieSearchResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Successful response received")
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: Response body = ${responseBody?.let { "results size: ${it.results?.size}" } ?: "null"}")
                    val movies = responseBody?.results ?: emptyList()
                    adapter.updateMovies(movies)
                } else {
                    Log.d(TAG, "onResponse: Response was not successful")
                    adapter.updateMovies(emptyList()) // Handle error response
                }
            }

            override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e(TAG, "onFailure: Error during API call", t)
                adapter.updateMovies(emptyList()) // Handle failure
            }
        })
    }

    private fun showAddToWatchlistDialog(movie: Movie) {
        Log.d(TAG, "Showing add to watchlist dialog for movie: ${movie.title}")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.add_to_watchlist))
        builder.setMessage(getString(R.string.add_to_watchlist_message, movie.title))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            addToWatchlist(movie)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun addToWatchlist(movie: Movie) {
        Log.d(TAG, "Adding movie to watchlist: ${movie.title}")
        val movieSearch = MovieSearch(
            id = movie.id,
            overview = movie.overview,
            poster = movie.poster,
            title = movie.title,
            tomatometer = movie.tomatometer,
            trailer = movie.trailer,
            year = movie.year.toString()
        )

        val dbHelper = WatchlistDatabaseHelper(requireContext())
        try {
            Log.d(TAG, "Preparing to insert movie: ${movieSearch.title}")
            val result = dbHelper.addMovie(movieSearch)
            if (result) {
                Log.d(TAG, "${movieSearch.title} inserted successfully into the watchlist.")
                Toast.makeText(requireContext(), "${movieSearch.title} added to watchlist", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "Failed to insert ${movieSearch.title} into the watchlist.")
                Toast.makeText(requireContext(), "Failed to add ${movieSearch.title} to watchlist", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding movie to watchlist: ${e.message}", e)
        }
    }

    private fun navigateToMovieDetails(movieId: Int) { // Update this method
        Log.d(TAG, "Navigating to MovieDetails fragment")
        val movieDetailsFragment = MovieDetails()
        val bundle = Bundle()
        bundle.putInt("movieId", movieId)
        movieDetailsFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, movieDetailsFragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}

