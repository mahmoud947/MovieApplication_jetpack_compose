package com.example.movieapplicationjetpackcompose.ui.screens.favorite

import com.example.core.base.ViewEvent
import com.example.core.base.ViewSideEffect
import com.example.core.base.ViewState
import com.example.domain.models.Movie
import com.example.movieapplicationjetpackcompose.shared.SearchWidgetState


class FavoriteContract {

    sealed class Event : ViewEvent {
        data object FetchFavoriteMovies : Event()
        data object OnCloseSearch : Event()
        data object OnOpenSearch : Event()
        data class OnSearchQueryChange(val query: String) : Event()
        data class OnSearchTriggered(val query: String) : Event()
        data class AddToFavorite(val movie: Movie):Event()
        data class RemoveFromFavorite(val movieId:Int):Event()
    }

    sealed class SideEffects : ViewSideEffect {
        data class ErrorMessageSideEffect(val message: String?) : ViewSideEffect
        data class ShowSnackBar(val message: String?) : ViewSideEffect
    }

    data class State(
        override val loading: Boolean = false,
        val movies: List<Movie>? = null,
        val searchQuery: String = "",
        val searchWidgetState: SearchWidgetState = SearchWidgetState.CLOSED
    ) : ViewState
}