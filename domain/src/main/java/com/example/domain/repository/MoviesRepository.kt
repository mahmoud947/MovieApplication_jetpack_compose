package com.example.domain.repository

import com.example.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun search(query: String): List<Movie>
    suspend fun searchOnFavorite(query: String): List<Movie>

    suspend fun getFavoriteMovies(): List<Movie>
    suspend fun getFavoriteMoviesFlow(): Flow<List<Movie>>

    suspend fun addToFavorite(movie: Movie)

    suspend fun removeFromFavorite(id: Int)
}
