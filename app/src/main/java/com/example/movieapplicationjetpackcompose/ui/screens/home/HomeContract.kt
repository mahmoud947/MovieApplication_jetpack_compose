package com.example.movieapplicationjetpackcompose.ui.screens.home

import com.example.core.base.ViewEvent
import com.example.core.base.ViewSideEffect
import com.example.core.base.ViewState
import com.example.domain.models.Movie
import com.example.movieapplicationjetpackcompose.components.SearchWidgetState


class HomeContract {

    sealed class Event : ViewEvent {
        data object FetchMovies : Event()
        data object OnCloseSearch : Event()
        data object OnOpenSearch : Event()
        data class OnSearchQueryChange(val query: String) : Event()
        data class OnSearchTriggered(val query: String) : Event()
    }

    sealed class SideEffects : ViewSideEffect {
        data class ShowErrorMessage(val errorMessage: String) : SideEffects()
    }

    data class State(
        override val loading: Boolean = false,
        val movies: List<Movie>? = null,
        val searchQuery: String = "",
        val searchWidgetState: SearchWidgetState = SearchWidgetState.CLOSED
    ) : ViewState
}