package com.example.movieapplicationjetpackcompose.ui.screens.details

import com.example.core.base.BaseViewModel
import com.example.core.utils.Resource
import com.example.core.utils.handle
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.usecase.GetMovieVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase
) : BaseViewModel<DetailsContract.Event, DetailsContract.State>() {
    override fun setInitialState(): DetailsContract.State = DetailsContract.State()

    override fun handleEvents(event: DetailsContract.Event) {
        when (event) {
            is DetailsContract.Event.FetchMovieVideos -> getMovieVideos(event.movieId)
            is DetailsContract.Event.FetchMovieDetails -> getMovieDetails(event.movieId)
            DetailsContract.Event.OnStartVideo -> onStartVideo()
            DetailsContract.Event.OnStopVideo -> onStopVideo()
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
}