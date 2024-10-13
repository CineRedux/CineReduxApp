package com.example.cineredux_v2

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "movies")
data class CachedMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val year: Int,
    val poster: String,
    val tomatometer: String?,
    val trailer: String?
)

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<CachedMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<CachedMovie>)
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): CachedMovie?


}
