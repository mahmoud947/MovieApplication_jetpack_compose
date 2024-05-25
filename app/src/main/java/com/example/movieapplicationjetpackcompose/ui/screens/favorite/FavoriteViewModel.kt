package com.example.movieapplicationjetpackcompose.ui.screens.favorite

import com.example.core.base.BaseViewModel
import com.example.core.utils.Resource
import com.example.core.utils.handle
import com.example.domain.models.Movie
import com.example.domain.usecase.GetFavoriteMoviesUseCase
import com.example.domain.usecase.SearchOnFavoriteMoviesUseCase
import com.example.movieapplicationjetpackcompose.components.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val searchOnFavoriteMoviesUseCase: SearchOnFavoriteMoviesUseCase
) : BaseViewModel<FavoriteContract.Event, FavoriteContract.State>() {
    override fun setInitialState(): FavoriteContract.State = FavoriteContract.State()

    private val _movies:MutableList<Movie> = mutableListOf()

    override fun handleEvents(event: FavoriteContract.Event) {
        when (event) {
            FavoriteContract.Event.FetchMovies -> fetchMovies()
            FavoriteContract.Event.OnCloseSearch -> setState { copy(searchWidgetState = SearchWidgetState.CLOSED, movies = _movies) }
            FavoriteContract.Event.OnOpenSearch -> setState { copy(searchWidgetState = SearchWidgetState.OPENED) }
            is FavoriteContract.Event.OnSearchQueryChange -> setState { copy(searchQuery = event.query) }
            is FavoriteContract.Event.OnSearchTriggered -> search(query = event.query)
        }
    }

    private fun search(query: String) {
        launchCoroutine(Dispatchers.IO) {
            searchOnFavoriteMoviesUseCase(input = query).collectLatest { resource: Resource<List<Movie>> ->
                resource.handle(onLoading = {
                    setState { copy(loading = true) }
                }, onSuccess = { movies ->
                    setState { copy(loading = false, movies = movies) }
                }, onError = {
                    setState { copy(loading = true) }
                })
            }

        }
    }

    private fun fetchMovies() {
        launchCoroutine(Dispatchers.IO) {
            getFavoriteMoviesUseCase().collectLatest { resource: Resource<List<Movie>> ->
                resource.handle(onLoading = {
                    setState { copy(loading = true) }
                }, onSuccess = { movies ->
                    _movies.addAll(movies)
                    setState { copy(loading = false, movies = _movies) }
                }, onError = {
                    setState { copy(loading = true) }
                })
            }

        }
    }
}