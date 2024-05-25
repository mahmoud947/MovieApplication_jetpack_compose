package com.example.domain.usecase

import com.example.core.base.BaseSuspendIOUseCase
import com.example.core.utils.Resource
import com.example.domain.errors.ExceptionHandler
import com.example.domain.models.Movie
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddMoviesToFavoriteUseCase @Inject constructor(
    private val repository: MoviesRepository
) : BaseSuspendIOUseCase<Movie, Flow<Resource<Unit>>> {
    override suspend fun invoke(input: Movie): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading)
            val result = repository.addToFavorite(input)
            emit(Resource.Success(result))
        }.catch {
            emit(ExceptionHandler.resolveError(it))
        }
    }
}