package com.example.domain.usecase

import com.example.core.base.BaseSuspendIOUseCase
import com.example.core.base.BaseSuspendOUseCase
import com.example.core.utils.Resource
import com.example.domain.errors.ExceptionHandler
import com.example.domain.models.Movie
import com.example.domain.models.MovieVideo
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieVideosUseCase @Inject constructor(
    private val repository: MoviesRepository
) : BaseSuspendIOUseCase<Int,Flow<Resource<List<MovieVideo>>>> {
    override suspend fun invoke(input: Int): Flow<Resource<List<MovieVideo>>> {
        return flow {
            emit(Resource.Loading)
            val result = repository.getMovieVideo(input)
            emit(Resource.Success(result))
        }.catch {
            emit(ExceptionHandler.resolveError(it))
        }
    }
}