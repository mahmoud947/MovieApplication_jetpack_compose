package com.example.movieapplicationjetpackcompose.ui.screens.home

import com.example.core.base.BaseViewModel
import com.example.domain.repository.MoviesRepository
import com.example.movieapplicationjetpackcompose.components.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MoviesRepository
) : BaseViewModel<HomeContract.Event, HomeContract.State>() {
    override fun setInitialState(): HomeContract.State = HomeContract.State()

    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            HomeContract.Event.FetchMovies -> fetchMovies()
            HomeContract.Event.OnCloseSearch -> setState { copy(searchWidgetState = SearchWidgetState.CLOSED) }
            HomeContract.Event.OnOpenSearch -> setState { copy(searchWidgetState = SearchWidgetState.OPENED) }
            is HomeContract.Event.OnSearchQueryChange -> setState { copy(searchQuery = event.query) }
        }
    }

    private fun fetchMovies() {
        launchCoroutine {
            val movies = repository.getPopularMovies()
            setState { copy(loading = false, movies = movies) }
        }


    }
}