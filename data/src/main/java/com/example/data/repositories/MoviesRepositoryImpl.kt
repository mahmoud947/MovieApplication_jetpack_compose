package com.example.data.repositories

import com.example.data.datasource.local.dao.MovieDao
import com.example.data.datasource.remote.service.MovieService
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.domain.models.Movie
import com.example.domain.models.MovieCategory
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val service: MovieService,
    private val dao: MovieDao
) : MoviesRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        val result = service.getMoviesByCategory(category = MovieCategory.Popular.endPoint).results

        return result.map {
            val isFavorite = dao.isFavorite(it.id ?: 0) != null
            it.toDomain(isFavorite = isFavorite)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        val result = service.getMovieDetails(movieId = movieId)
        return result.toDomain()
    }

    override suspend fun getMovieVideo(id: Int): List<MovieVideo> {
        val result = service.getMovieVideos(movieId = id).results
        return result.map { it.toDomain() }
    }

    override suspend fun search(query: String): List<Movie> {
        val result = service.search(query = query).results

        return result.map {
            val isFavorite = dao.isFavorite(it.id ?: 0) != null
            it.toDomain(isFavorite = isFavorite)
        }
    }

    override suspend fun searchOnFavorite(query: String): List<Movie> {
        return dao.searchMoviesByTitle(title = query).map { it.toDomain() }
    }

    override suspend fun getFavoriteMovies(): List<Movie> {
        return dao.getFavoriteMovies().map { it.toDomain() }
    }

    override suspend fun getFavoriteMoviesFlow(): Flow<List<Movie>> {
        return dao.getFavoriteMoviesFlow().map { movies ->
            movies.map { it.toDomain() }
        }
    }

    override suspend fun addToFavorite(movie: Movie) {
        dao.addToFavorite(movie = movie.toEntity())
    }

    override suspend fun removeFromFavorite(id: Int) {
        dao.removeFromFavorite(movieId = id)
    }

}