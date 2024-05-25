package com.example.movieapplicationjetpackcompose.di

import com.example.data.repositories.MoviesRepositoryImpl
import com.example.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {
    @Binds
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository
}