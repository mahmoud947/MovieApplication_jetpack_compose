package com.example.movieapplicationjetpackcompose.ui.screens.favorite

import com.example.core.base.BaseViewModel
import com.example.core.utils.Resource
import com.example.core.utils.handle
import com.example.domain.models.Movie
import com.example.domain.usecase.AddMoviesToFavoriteUseCase
import com.example.domain.usecase.GetFavoriteMoviesFlowUseCase
import com.example.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.example.domain.usecase.SearchOnFavoriteMoviesUseCase
import com.example.movieapplicationjetpackcompose.shared.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMoviesFlowUseCase: GetFavoriteMoviesFlowUseCase,
    private val searchOnFavoriteMoviesUseCase: SearchOnFavoriteMoviesUseCase,
    private val addMoviesToFavoriteUseCase: AddMoviesToFavoriteUseCase,
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase
) : BaseViewModel<FavoriteContract.Event, FavoriteContract.State>() {
    override fun setInitialState(): FavoriteContract.State = FavoriteContract.State()

    private val _movies: MutableList<Movie> = mutableListOf()

    override fun handleEvents(event: FavoriteContract.Event) {
        when (event) {
            FavoriteContract.Event.FetchFavoriteMovies -> fetchMovies()
            FavoriteContract.Event.OnCloseSearch -> setState {
                copy(
                    searchWidgetState = SearchWidgetState.CLOSED,
                    movies = _movies
                )
            }

            FavoriteContract.Event.OnOpenSearch -> setState { copy(searchWidgetState = SearchWidgetState.OPENED) }
            is FavoriteContract.Event.OnSearchQueryChange -> setState { copy(searchQuery = event.query) }
            is FavoriteContract.Event.OnSearchTriggered -> search(query = event.query)
            is FavoriteContract.Event.AddToFavorite -> addToFavorite(movie = event.movie)
            is FavoriteContract.Event.RemoveFromFavorite -> removeFromFavorite(id = event.movieId)
        }
    }

    private fun removeFromFavorite(id: Int) {
        launchCoroutine(Dispatchers.IO) {
            removeMovieFromFavoriteUseCase(id).collectLatest { resource: Resource<Unit> ->
                resource.handle(onLoading = {
                    setState { copy(loading = true) }
                }, onSuccess = {
                    setState { copy(loading = false) }
                    showSnackBar(message = "Movie removed from favorite successfully")
                }, onError = {
                    setState { copy(loading = true) }
                    handleError(it)
                })
            }
        }
    }

    private fun addToFavorite(movie: Movie) {
        launchCoroutine(Dispatchers.IO) {
            addMoviesToFavoriteUseCase(movie).collectLatest { resource: Resource<Unit> ->
                resource.handle(onLoading = {
                    setState { copy(loading = true) }
                }, onSuccess = {
                    setState { copy(loading = false) }
                   showSnackBar(message = "Movie Added to favorite successfully")
                }, onError = {
                    setState { copy(loading = false) }
                    handleError(it)
                })
            }
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
            getFavoriteMoviesFlowUseCase().collectLatest { movies ->
                _movies.clear()
                _movies.addAll(movies)
                setState { copy(loading = false, movies = _movies) }
            }
        }
    }

    private fun handleError(exception: Throwable) {
        updateLoadingState(false)
        showErrorMessage(exception.message ?: "An unknown error occurred")
    }
    private fun updateLoadingState(isLoading: Boolean) {
        setState { copy(loading = isLoading) }
    }

    private fun showErrorMessage(message: String) {
        launchCoroutine{ setEffect { FavoriteContract.SideEffects.ErrorMessageSideEffect(message) } }
    }

    private fun showSnackBar(message: String) {
        launchCoroutine{ setEffect { FavoriteContract.SideEffects.ShowSnackBar(message) } }
    }
}