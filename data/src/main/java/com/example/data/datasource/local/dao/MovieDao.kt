package com.example.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.data.datasource.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    @Upsert
    suspend fun addToFavorite(movie: MovieEntity)

    @Query("SELECT * FROM movieentity")
    suspend fun getFavoriteMovies(): List<MovieEntity>

    @Query("SELECT * FROM movieentity")
    fun getFavoriteMoviesFlow(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movieentity WHERE id=:movieId")
    suspend fun removeFromFavorite(movieId: Int): Int

    @Query("SELECT id FROM movieentity WHERE id=:movieId")
    suspend fun isFavorite(movieId: Int): Int?

    @Query("SELECT id FROM movieentity WHERE id=:movieId")
    fun isFavoriteFlow(movieId: Int): Flow<Int?>


    @Query("SELECT * FROM movieentity WHERE title LIKE '%' || :title || '%'")
    suspend fun searchMoviesByTitle(title: String): List<MovieEntity>

    @Query("DELETE FROM movieentity")
    suspend fun clearAll()
}