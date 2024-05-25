package com.example.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.data.datasource.local.entity.MovieEntity
import com.example.domain.models.Movie
import com.example.domain.models.MovieCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Upsert
    suspend fun addToFavorite(movies:MovieEntity)

    @Query("SELECT * FROM movieentity")
    suspend fun getFavoriteMovies(category: MovieCategory):List<MovieEntity>

    @Query("DELETE FROM movieentity WHERE id=:movieId")
    suspend fun removeFromFavorite(movieId: Int):MovieEntity

    @Query("DELETE FROM movieentity")
    suspend fun clearAll()
}