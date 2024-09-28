import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cineredux_v2.Movie
import com.example.cineredux_v2.R
import com.example.cineredux_v2.WatchlistAdapter
import com.example.cineredux_v2.WatchlistDatabaseHelper

class WatchlistFragment : Fragment() {

    private var dbHelper: WatchlistDatabaseHelper? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: WatchlistAdapter? = null
    private val movieList = ArrayList<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_watchlist, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_watchlist)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        dbHelper = WatchlistDatabaseHelper(requireContext())
        Log.d("WatchlistFragment", "dbHelper initialized: $dbHelper")

        loadMoviesFromDatabase()

        adapter = WatchlistAdapter(movieList)
        recyclerView?.adapter = adapter

        return view
    }


    private fun loadMoviesFromDatabase() {
        val cursor = dbHelper?.allMovies
        Log.d("WatchlistFragment", "Cursor retrieved: $cursor")

        if (cursor == null) {
            Log.e("WatchlistFragment", "Cursor is null")
            return
        }

        val columnNames = cursor.columnNames.joinToString(", ")
        Log.d("WatchlistFragment", "Column names: $columnNames")

        try {
            if (cursor.moveToFirst()) {
                do {
                    val idColumnIndex = cursor.getColumnIndexOrThrow("id")
                    val titleColumnIndex = cursor.getColumnIndexOrThrow("title")
                    val overviewColumnIndex = cursor.getColumnIndexOrThrow("overview")
                    val posterUrlColumnIndex = cursor.getColumnIndexOrThrow("poster_url")
                    val tomatometerColumnIndex = cursor.getColumnIndexOrThrow("tmdbScore")  // Added this to retrieve Tomatometer
                    val trailerUrlColumnIndex = cursor.getColumnIndexOrThrow("trailer_url")  // Added this to retrieve trailer URL

                    val id = cursor.getInt(idColumnIndex)
                    val title = cursor.getString(titleColumnIndex)
                    val overview = cursor.getString(overviewColumnIndex)
                    val posterUrl = cursor.getString(posterUrlColumnIndex) ?: "default_poster_url"
                    val tomatometer = cursor.getString(tomatometerColumnIndex) ?: "N/A"  // Handle missing score
                    val trailerUrl = cursor.getString(trailerUrlColumnIndex) ?: "No Trailer"


                    Log.d("WatchlistFragment", "Retrieved movie - Poster URL: $posterUrl, Tomatometer: $tomatometer, Trailer: $trailerUrl")

                    // Add the movie to the movieList
                    movieList.add(Movie(id, title, overview, posterUrl, tomatometer, trailerUrl))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("WatchlistFragment", "Error processing cursor data", e)
        } finally {
            cursor.close()
        }
    }
}
