package com.example.movieapplicationjetpackcompose.di

import android.content.Context
import com.example.data.datasource.local.MovieDatabase
import com.example.data.datasource.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase =
        MovieDatabase.getInstance(context = context)

    @Provides
    @Singleton
    fun provideMovieDao(
        database: MovieDatabase
    ): MovieDao =
        database.movieDao


}