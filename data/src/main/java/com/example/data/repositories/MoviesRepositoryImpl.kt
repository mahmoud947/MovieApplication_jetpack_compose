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
        val result = service.getMoviesByCategory(MovieCategory.Popular.name).results
        return result.map { it.toDomain() }
    }


}