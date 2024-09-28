package com.example.cineredux_v2


/**
class WatchlistDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_WATCHLIST_TABLE = ("CREATE TABLE " + TABLE_WATCHLIST + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  // Correct type for ID
                + COLUMN_TITLE + " TEXT,"                            // TEXT for title
                + COLUMN_OVERVIEW + " TEXT,"                         // TEXT for overview
                + COLUMN_POSTER_URL + " TEXT" + ")")                 // TEXT for poster_url
        db.execSQL(CREATE_WATCHLIST_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLIST)
        onCreate(db)
    }

    // Add a movie to the watchlist
    fun addMovie(title: String?, overview: String?, posterUrl: String?) {
        val db = this.writableDatabase
        val values = ContentValues()

        // Correctly insert values into the ContentValues object
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_OVERVIEW, overview)
        values.put(COLUMN_POSTER_URL, posterUrl)

        // Insert the values into the database (auto-increment for COLUMN_ID)
        db.insert(TABLE_WATCHLIST, null, values)
        db.close()
    }


    val allMovies: Cursor
        // Get all movies from the watchlist
        get() {
            val db = this.readableDatabase
            return db.query(TABLE_WATCHLIST, null, null, null, null, null, null)
        }

    companion object {
        private const val DATABASE_NAME = "watchlist.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_WATCHLIST = "watchlist"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_OVERVIEW = "overview"
        private const val COLUMN_POSTER_URL = "poster_url"
    }
}
**/

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class WatchlistDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating watchlist table")
        // Create the watchlist table with correct column names and types
        val CREATE_WATCHLIST_TABLE = """
            CREATE TABLE $TABLE_WATCHLIST (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_OVERVIEW TEXT,
                $COLUMN_POSTER_URL TEXT,
                $COLUMN_TOMATOEMETER TEXT,  
                $COLUMN_TRAILER_URL TEXT
            )
        """.trimIndent()
        db.execSQL(CREATE_WATCHLIST_TABLE)
        Log.d(TAG, "Watchlist table created successfully")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from version $oldVersion to $newVersion")
        // Drop the old table if it exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WATCHLIST")
        Log.d(TAG, "Old watchlist table dropped if it existed")
        // Create the new table
        onCreate(db)
    }

    // Method to add a movie to the watchlist
    fun addMovie(movie: Movie) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, movie.title)
            put(COLUMN_OVERVIEW, movie.overview)
            put(COLUMN_POSTER_URL, movie.poster)
            put(COLUMN_TOMATOEMETER, movie.tomatometer ?: "N/A")  // Ensure score is correctly formatted
            put(COLUMN_TRAILER_URL, movie.trailer ?: "No Trailer")  // Handle missing trailer case
        }

        try {
            val result = db.insert(TABLE_WATCHLIST, null, values)
            if (result == -1L) {
                Log.e(TAG, "Error inserting movie: ${movie.title}")
            } else {
                Log.d(TAG, "Movie inserted successfully: ${movie.title}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while inserting movie: ${e.message}", e)
        } finally {
            db.close()
        }
    }

    // Method to retrieve all movies in the watchlist
    val allMovies: Cursor
        get() {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_WATCHLIST", null)
        }

    // Method to delete a movie from the watchlist by id
    fun deleteMovieById(id: Int) {
        val db = this.writableDatabase
        try {
            val result = db.delete(TABLE_WATCHLIST, "$COLUMN_ID=?", arrayOf(id.toString()))
            if (result == 0) {
                Log.e(TAG, "Error deleting movie with id: $id")
            } else {
                Log.d(TAG, "Movie with id: $id deleted successfully")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while deleting movie: ${e.message}", e)
        } finally {
            db.close()
        }
    }

    companion object {
        private const val DATABASE_NAME = "watchlist.db"
        private const val DATABASE_VERSION = 4

        // Table and column names
        private const val TABLE_WATCHLIST = "movies"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_OVERVIEW = "overview"
        private const val COLUMN_POSTER_URL = "poster_url"
        private const val COLUMN_TOMATOEMETER = "tmdbScore"
        private const val COLUMN_TRAILER_URL = "trailer_url"

        private const val TAG = "WatchlistDatabaseHelper"
    }
}
