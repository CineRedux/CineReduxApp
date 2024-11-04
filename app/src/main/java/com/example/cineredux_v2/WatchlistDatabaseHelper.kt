package com.example.cineredux_v2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class WatchlistDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating watchlist table")
        // Create the watchlist table with the correct column names and types
        val CREATE_WATCHLIST_TABLE = """
            CREATE TABLE $TABLE_WATCHLIST (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_OVERVIEW TEXT,
                $COLUMN_POSTER_URL TEXT,
                $COLUMN_TOMATOEMETER TEXT,  
                $COLUMN_TRAILER_URL TEXT,
                $COLUMN_YEAR TEXT 
            )
        """.trimIndent()
        db.execSQL(CREATE_WATCHLIST_TABLE)
        Log.d(TAG, "Watchlist table created successfully")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from version $oldVersion to $newVersion")
        // In a real-world scenario, you could also migrate data. But for now, drop the old table and create a new one.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WATCHLIST")
        Log.d(TAG, "Old watchlist table dropped")
        onCreate(db)
    }
    fun isMovieInWatchlist(movieId: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_WATCHLIST,
            arrayOf(COLUMN_ID),
            "$COLUMN_ID = ?",
            arrayOf(movieId.toString()),
            null,
            null,
            null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun addMovie(movie: MovieSearch): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, movie.title)
            put(COLUMN_OVERVIEW, movie.overview)
            put(COLUMN_POSTER_URL, movie.poster)
            put(COLUMN_TOMATOEMETER, movie.tomatometer ?: "N/A")
            put(COLUMN_TRAILER_URL, movie.trailer ?: "No Trailer")
            put(COLUMN_YEAR, movie.year)
        }

        Log.d(TAG, "Inserting movie: ${movie.title}")
        Log.d(TAG, "Tomatometer: ${movie.tomatometer ?: "No value"}")
        Log.d(TAG, "Trailer: ${movie.trailer ?: "No value"}")
        return try {
            Log.d(TAG, "Inserting movie: ${movie.title}")
            val result = db.insert(TABLE_WATCHLIST, null, values)
            if (result == -1L) {
                Log.e(TAG, "Failed to insert movie: ${movie.title}")
                false
            } else {
                Log.d(TAG, "Movie inserted successfully: ${movie.title} with ID: $result")
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while inserting movie: ${e.message}", e)
            false
        } finally {
            db.close()
        }
    }


    fun getWatchlistMovies(): List<MovieSearch> {
        val movies = mutableListOf<MovieSearch>()
        val db = this.readableDatabase

        db.rawQuery("SELECT * FROM $TABLE_WATCHLIST", null).use { cursor ->
            if (cursor.moveToFirst()) {
                Log.d(TAG, "Retrieving movies from watchlist")
                do {
                    val movie = MovieSearch(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        overview = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW)),
                        poster = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_URL)),
                        tomatometer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOMATOEMETER)),
                        trailer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRAILER_URL)),
                        year = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_YEAR)) // Retrieve the year as a string
                    )
                    movies.add(movie)
                    Log.d(TAG, "Retrieved movie: ${movie.title} with ID: ${movie.id}")
                } while (cursor.moveToNext())
                Log.d(TAG, "Total movies retrieved: ${movies.size}")
            } else {
                Log.d(TAG, "No movies found in the watchlist")
            }
        }

        return movies
    }

    fun deleteMovieById(id: Int): Boolean {
        val db = this.writableDatabase
        return try {
            Log.d(TAG, "Deleting movie with ID: $id")
            val result = db.delete(TABLE_WATCHLIST, "$COLUMN_ID=?", arrayOf(id.toString()))
            if (result == 0) {
                Log.e(TAG, "Failed to delete movie with ID: $id. Movie may not exist.")
                false
            } else {
                Log.d(TAG, "Movie with ID: $id deleted successfully")
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while deleting movie: ${e.message}", e)
            false
        } finally {
            db.close()
        }
    }

    companion object {
        private const val DATABASE_NAME = "watchlist.db"
        private const val DATABASE_VERSION = 7

        private const val TABLE_WATCHLIST = "movies"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_OVERVIEW = "overview"
        private const val COLUMN_POSTER_URL = "poster_url"
        private const val COLUMN_TOMATOEMETER = "tmdbScore"
        private const val COLUMN_TRAILER_URL = "trailer_url"
        private const val COLUMN_YEAR = "year"

        private const val TAG = "WatchlistDatabaseHelper"
    }
}
