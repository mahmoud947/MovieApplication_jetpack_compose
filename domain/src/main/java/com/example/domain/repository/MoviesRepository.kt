package com.example.domain.repository

import com.example.domain.models.Movie
import com.example.domain.models.MovieCategory

interface MoviesRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun search(query:String): List<Movie>
}
