package com.example.movieapplicationjetpackcompose.ui.screens.details

import com.example.core.base.BaseViewModel
import com.example.core.utils.Resource
import com.example.core.utils.handle
import com.example.domain.models.Movie
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo
import com.example.domain.usecase.AddMoviesToFavoriteUseCase
import com.example.domain.usecase.CheckIsFavoriteMovieUseCase
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.usecase.GetMovieVideosUseCase
import com.example.domain.usecase.RemoveMovieFromFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase,
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase,
    private val addMoviesToFavoriteUseCase: AddMoviesToFavoriteUseCase,
    private val checkIsFavoriteMovieUseCase: CheckIsFavoriteMovieUseCase
) : BaseViewModel<DetailsContract.Event, DetailsContract.State>() {
    override fun setInitialState(): DetailsContract.State = DetailsContract.State()

    override fun handleEvents(event: DetailsContract.Event) {
        when (event) {
            is DetailsContract.Event.FetchMovieVideos -> getMovieVideos(event.movieId)
            is DetailsContract.Event.FetchMovieDetails -> getMovieDetails(event.movieId)
            is DetailsContract.Event.OnStartVideo -> onStartVideo()
            is DetailsContract.Event.OnStopVideo -> onStopVideo()
            is DetailsContract.Event.AddToFavorite -> addToFavorite(event.movie)
            is DetailsContract.Event.RemoveFromFavorite -> removeFromFavorite(event.movieId)
        }
    }

    private fun onStopVideo() {
        setState { copy(isVideoStarted = false) }
    }

    private fun onStartVideo() {
        setState { copy(isVideoStarted = true) }
    }

    private fun getMovieDetails(movieId: Int) {
        launchCoroutine(dispatcher = Dispatchers.IO) {
            val movieDetails = async {
                getMovieDetailsUseCase(movieId).collectLatest { resource: Resource<MovieDetails> ->
                    resource.handle(onLoading = {
                        setState { copy(loading = true) }
                    }, onSuccess = {
                        setState { copy(movie = it) }
                    }, onError = {
                        setState { copy(loading = false) }
                    })
                }
            }

            val isFavorite = async {
                checkIsFavoriteMovieUseCase(movieId).collectLatest { isFavorite ->
                    setState { copy(isFavorite = isFavorite) }
                }
            }

            awaitAll(movieDetails, isFavorite)
        }
    }

    private fun getMovieVideos(movieId: Int) {
        launchCoroutine(dispatcher = Dispatchers.IO) {
            getMovieVideosUseCase(movieId).collectLatest { resource: Resource<List<MovieVideo>> ->
                resource.handle(onLoading = {
                    setState { copy(loading = true) }
                }, onSuccess = {
                    setState { copy(videos = it) }
                }, onError = {
                    setState { copy(loading = false) }
                })
            }
        }
    }


    private fun removeFromFavorite(id: Int) {
        launchCoroutine(Dispatchers.IO) {
            removeMovieFromFavoriteUseCase(id).collectLatest { resource: Resource<Unit> ->
                resource.handle(onLoading = {
                    setState { copy(loading = true) }
                }, onSuccess = {

                }, onError = {
                    setState { copy(loading = true) }
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
                }, onError = {
                    setState { copy(loading = true) }
                })
            }
        }
    }

}