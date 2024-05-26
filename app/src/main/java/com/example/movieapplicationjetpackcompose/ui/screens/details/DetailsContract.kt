package com.example.movieapplicationjetpackcompose.ui.screens.details

import com.example.core.base.ViewEvent
import com.example.core.base.ViewSideEffect
import com.example.core.base.ViewState
import com.example.domain.models.Movie
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo
import com.example.movieapplicationjetpackcompose.components.SearchWidgetState
import com.example.movieapplicationjetpackcompose.ui.screens.favorite.FavoriteContract


class DetailsContract {
    sealed class Event : ViewEvent {
        data class FetchMovieVideos(val movieId: Int) : Event()
        data class FetchMovieDetails(val movieId: Int) : Event()
        data object OnStartVideo : Event()
        data object OnStopVideo : Event()

    }

    sealed class SideEffects : ViewSideEffect {
        data class ShowErrorMessage(val errorMessage: String) : SideEffects()
    }

    data class State(
        override val loading: Boolean = false,
        val videos: List<MovieVideo>? = null,
        val movie: MovieDetails? = null,
        val isVideoStarted:Boolean = false
    ) : ViewState
}