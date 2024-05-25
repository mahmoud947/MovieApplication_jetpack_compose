package com.example.domain.usecase

import com.example.core.base.BaseIOUseCase
import com.example.core.base.BaseOUseCase
import com.example.core.base.BaseSuspendOUseCase
import com.example.core.utils.Resource
import com.example.domain.errors.ExceptionHandler
import com.example.domain.models.Movie

import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) : BaseSuspendOUseCase<Flow<Resource<List<Movie>>>> {
    override suspend fun invoke(): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading)
            val result = repository.getFavoriteMovies()
            emit(Resource.Success(result))
        }.catch {
            emit(ExceptionHandler.resolveError(it))
        }
    }
}