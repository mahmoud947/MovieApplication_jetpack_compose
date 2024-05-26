package com.example.domain.usecase

import com.example.core.base.BaseSuspendIOUseCase
import com.example.core.base.BaseSuspendOUseCase
import com.example.core.utils.Resource
import com.example.domain.errors.ExceptionHandler
import com.example.domain.models.Movie
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MoviesRepository
) : BaseSuspendIOUseCase<Int,Flow<Resource<MovieDetails>>> {
    override suspend fun invoke(input: Int): Flow<Resource<MovieDetails>> {
        return flow {
            emit(Resource.Loading)
            val result = repository.getMovieDetails(input)
            emit(Resource.Success(result))
        }.catch {
            emit(ExceptionHandler.resolveError(it))
        }
    }
}