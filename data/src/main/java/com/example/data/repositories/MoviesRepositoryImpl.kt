package com.example.data.repositories

import com.example.data.datasource.remote.service.MovieService
import com.example.data.mapper.toDomain
import com.example.domain.models.Movie
import com.example.domain.models.MovieCategory
import com.example.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val service: MovieService
) : MoviesRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        val result = service.getMoviesByCategory( category = MovieCategory.Popular.endPoint).results
        return result.map { it.toDomain() }
    }

    override suspend fun search(query: String): List<Movie> {
        val result = service.search(query = query).results
        return result.map { it.toDomain() }
    }


}