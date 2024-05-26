package com.example.domain.repository

import com.example.domain.models.Movie
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getPopularMovies(): List<Movie>

    suspend fun getMovieDetails(movieId:Int):MovieDetails
    suspend fun getMovieVideo(id: Int): List<MovieVideo>
    suspend fun search(query: String): List<Movie>
    suspend fun searchOnFavorite(query: String): List<Movie>

    suspend fun getFavoriteMovies(): List<Movie>
    suspend fun getFavoriteMoviesFlow(): Flow<List<Movie>>

    suspend fun addToFavorite(movie: Movie)

    suspend fun isFavoriteMovie(movieId: Int):Boolean
    suspend fun isFavoriteMovieFlow(movieId: Int):Flow<Boolean>
    suspend fun removeFromFavorite(id: Int)
}
