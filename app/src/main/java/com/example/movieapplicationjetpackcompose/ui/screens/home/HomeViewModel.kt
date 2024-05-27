package com.example.movieapplicationjetpackcompose.ui.screens.home

import com.example.core.base.BaseViewModel
import com.example.core.utils.Resource
import com.example.core.utils.handle
import com.example.domain.models.Movie
import com.example.domain.usecase.AddMoviesToFavoriteUseCase
import com.example.domain.usecase.GetPopularMoviesUseCase
import com.example.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.example.domain.usecase.SearchMoviesUseCase
import com.example.movieapplicationjetpackcompose.shared.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val addMoviesToFavoriteUseCase: AddMoviesToFavoriteUseCase,
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase
) : BaseViewModel<HomeContract.Event, HomeContract.State>() {
    override fun setInitialState(): HomeContract.State = HomeContract.State()

    private val _movies: MutableList<Movie> = mutableListOf()

    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            HomeContract.Event.FetchMovies -> fetchMovies()
            HomeContract.Event.OnCloseSearch -> closeSearch()
            HomeContract.Event.OnOpenSearch -> openSearch()
            is HomeContract.Event.OnSearchQueryChange -> updateSearchQuery(event.query)
            is HomeContract.Event.OnSearchTriggered -> search(event.query)
            is HomeContract.Event.AddToFavorite -> addToFavorite(event.movie)
            is HomeContract.Event.RemoveFromFavorite -> removeFromFavorite(event.movieId)
        }
    }

    private fun fetchMovies() {
        launchCoroutine(Dispatchers.IO) {
            getPopularMoviesUseCase().collectLatest { resource: Resource<List<Movie>> ->
                resource.handle(
                    onLoading = { updateLoadingState(true) },
                    onSuccess = { movies ->
                        _movies.clear()
                        _movies.addAll(movies)
                        setState { copy(loading = false, movies = _movies) }
                    },
                    onError = { handleError(it) }
                )
            }
        }
    }

    private fun search(query: String) {
        launchCoroutine(Dispatchers.IO) {
            searchMoviesUseCase(input = query).collectLatest { resource: Resource<List<Movie>> ->
                resource.handle(
                    onLoading = { updateLoadingState(true) },
                    onSuccess = { movies ->
                        setState { copy(loading = false, movies = movies) }
                    },
                    onError = { handleError(it) }
                )
            }
        }
    }

    private fun addToFavorite(movie: Movie) {
        launchCoroutine(Dispatchers.IO) {
            addMoviesToFavoriteUseCase(movie).collectLatest { resource: Resource<Unit> ->
                resource.handle(
                    onLoading = { updateLoadingState(true) },
                    onSuccess = {
                        _movies.find { it.id == movie.id }?.apply { isFavorite = true }
                        setState { copy(loading = false, movies = _movies.toList()) }
                        showSnackBar("Movie added to favorite successfully")
                    },
                    onError = { handleError(it) }
                )
            }
        }
    }

    private fun removeFromFavorite(id: Int) {
        launchCoroutine(Dispatchers.IO) {
            removeMovieFromFavoriteUseCase(id).collectLatest { resource: Resource<Unit> ->
                resource.handle(
                    onLoading = { updateLoadingState(true) },
                    onSuccess = {
                        _movies.find { it.id == id }?.apply { isFavorite = false }
                        setState { copy(loading = false, movies = _movies.toList()) }
                        showSnackBar("Movie removed from favorite successfully")
                    },
                    onError = { handleError(it) }
                )
            }
        }
    }

    private fun openSearch() {
        setState { copy(searchWidgetState = SearchWidgetState.OPENED) }
    }

    private fun closeSearch() {
        setState {
            copy(
                searchWidgetState = SearchWidgetState.CLOSED,
                movies = _movies
            )
        }
    }

    private fun updateSearchQuery(query: String) {
        setState { copy(searchQuery = query) }
    }

    private fun handleError(exception: Throwable) {
        updateLoadingState(false)
        showErrorMessage(exception.message ?: "An unknown error occurred")
    }

    private fun updateLoadingState(isLoading: Boolean) {
        setState { copy(loading = isLoading) }
    }

    private fun showErrorMessage(message: String) {
        launchCoroutine { setEffect { HomeContract.SideEffects.ErrorMessageSideEffect(message) } }
    }

    private fun showSnackBar(message: String) {
        launchCoroutine { setEffect { HomeContract.SideEffects.ShowSnackBar(message) } }
    }
}
