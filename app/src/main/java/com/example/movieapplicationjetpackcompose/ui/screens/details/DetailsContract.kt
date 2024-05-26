package com.example.movieapplicationjetpackcompose.ui.screens.details

import com.example.core.base.ViewEvent
import com.example.core.base.ViewSideEffect
import com.example.core.base.ViewState
import com.example.domain.models.Movie
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo


class DetailsContract {
    sealed class Event : ViewEvent {
        data class FetchMovieVideos(val movieId: Int) : Event()
        data class FetchMovieDetails(val movieId: Int) : Event()
        data object OnStartVideo : Event()
        data object OnStopVideo : Event()
        data class AddToFavorite(val movie: Movie): Event()
        data class RemoveFromFavorite(val movieId:Int): Event()

    }

    sealed class SideEffects : ViewSideEffect {
        data class ErrorMessageSideEffect(val message: String?) : ViewSideEffect
        data class ShowSnackBar(val message: String?) : ViewSideEffect
    }

    data class State(
        override val loading: Boolean = false,
        val videos: List<MovieVideo>? = null,
        val movie: MovieDetails? = null,
        val isFavorite:Boolean = false,
        val isVideoStarted:Boolean = false
    ) : ViewState
}