package com.example.movieapplicationjetpackcompose.ui.screens.details

import com.example.core.utils.Resource
import com.example.domain.models.Movie
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo
import com.example.domain.usecase.AddMoviesToFavoriteUseCase
import com.example.domain.usecase.CheckIsFavoriteMovieUseCase
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.usecase.GetMovieVideosUseCase
import com.example.domain.usecase.RemoveMovieFromFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DetailsViewModelTest {

    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mockk()
    private val getMovieVideosUseCase: GetMovieVideosUseCase = mockk()
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase = mockk()
    private val addMoviesToFavoriteUseCase: AddMoviesToFavoriteUseCase = mockk()
    private val checkIsFavoriteMovieUseCase: CheckIsFavoriteMovieUseCase = mockk()
    private lateinit var viewModel: DetailsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeAll
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailsViewModel(
            getMovieDetailsUseCase,
            getMovieVideosUseCase,
            removeMovieFromFavoriteUseCase,
            addMoviesToFavoriteUseCase,
            checkIsFavoriteMovieUseCase
        )
    }

    @AfterAll
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `fetchMovieDetails should update state with movie details and favorite status`() = runTest {
        // Arrange
        val movieId = 1
        val movieDetails = MovieDetails(
            adult = null,
            backdropUrl = null,
            belongsToCollection = null,
            budget = null,
            genres = listOf(),
            homepage = null,
            id = movieId,
            imdbId = null,
            originCountry = listOf(),
            originalLanguage = null,
            originalTitle = null,
            overview = null,
            popularity = null,
            posterUrl = null,
            productionCompanies = listOf(),
            productionCountries = listOf(),
            releaseDate = null,
            revenue = null,
            runtime = null,
            spokenLanguages = listOf(),
            status = null,
            tagline = null,
            title = null,
            video = null,
            voteAverage = null,
            voteCount = null
        )
        coEvery { getMovieDetailsUseCase(movieId) } returns flowOf(Resource.Success(movieDetails))
        coEvery { checkIsFavoriteMovieUseCase(movieId) } returns flowOf(true)

        viewModel.setState { copy(movie = movieDetails, isFavorite = true) }
        // Act
        viewModel.setEvent(DetailsContract.Event.FetchMovieDetails(movieId))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(movieDetails, state.movie)
        Assertions.assertTrue(state.isFavorite)
        Assertions.assertFalse(state.loading)
        coVerify { getMovieDetailsUseCase(movieId) }
        coVerify { checkIsFavoriteMovieUseCase(movieId) }
    }

    @Test
    fun `fetchMovieVideos should update state with movie videos`() = runTest {
        // Arrange
        val movieId = 1
        val movieVideos = listOf(
            MovieVideo(
                id = "1",
                key = "key",
                name = "Trailer",
                site = "YouTube",
                type = "Trailer",
                size = 3,
                publishedAt = "publish",
                iso6391 = "iso",
                iso31661 = "iso",
                official = false
            )
        )
        coEvery { getMovieVideosUseCase(movieId) } returns flowOf(Resource.Success(movieVideos))

        viewModel.setState { copy(videos = movieVideos) }
        // Act
        viewModel.setEvent(DetailsContract.Event.FetchMovieVideos(movieId))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(movieVideos, state.videos)
        Assertions.assertFalse(state.loading)
        coVerify { getMovieVideosUseCase(movieId) }
    }

    @Test
    fun `addToFavorite should update state when movie is added to favorites`() = runTest {
        // Arrange
        val movie = Movie(
            adult = false,
            backdropUrl = "BackdropUrl",
            id = 1,
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Overview",
            popularity = 100.0,
            posterUrl = "PosterUrl",
            releaseDate = "2022-01-01",
            title = "Title",
            video = false,
            voteAverage = 7.5,
            voteCount = 100,
            isFavorite = false
        )
        coEvery { addMoviesToFavoriteUseCase(any()) } returns flowOf(Resource.Success(Unit))

        // Act
        viewModel.setEvent(DetailsContract.Event.AddToFavorite(movie))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        coVerify { addMoviesToFavoriteUseCase(any()) }
    }

    @Test
    fun `removeFromFavorite should update state when movie is removed from favorites`() = runTest {
        // Arrange
        val movieId = 1
        coEvery { removeMovieFromFavoriteUseCase(movieId) } returns flowOf(Resource.Success(Unit))

        // Act
        viewModel.setEvent(DetailsContract.Event.RemoveFromFavorite(movieId))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        coVerify { removeMovieFromFavoriteUseCase(movieId) }
    }

    @Test
    fun `onStartVideo should set isVideoStarted to true`() = runTest {
        // Act
        viewModel.setEvent(DetailsContract.Event.OnStartVideo)
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertTrue(state.isVideoStarted)
    }

    @Test
    fun `onStopVideo should set isVideoStarted to false`() = runTest {
        // Act
        viewModel.setEvent(DetailsContract.Event.OnStopVideo)
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertFalse(state.isVideoStarted)
    }
}
