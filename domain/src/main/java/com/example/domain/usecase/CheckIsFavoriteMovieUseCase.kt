package com.example.domain.usecase

import com.example.core.base.BaseSuspendIOUseCase
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckIsFavoriteMovieUseCase @Inject constructor(
    private val repository: MoviesRepository
) : BaseSuspendIOUseCase<Int, Flow<Boolean>> {
    override suspend fun invoke(input: Int): Flow<Boolean> = repository.isFavoriteMovieFlow(input)

}