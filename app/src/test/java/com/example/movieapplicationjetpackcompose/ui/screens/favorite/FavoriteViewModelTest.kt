package com.example.movieapplicationjetpackcompose.ui.screens.favorite

import com.example.core.utils.Resource
import com.example.domain.models.Movie
import com.example.domain.usecase.AddMoviesToFavoriteUseCase
import com.example.domain.usecase.GetFavoriteMoviesFlowUseCase
import com.example.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.example.domain.usecase.SearchOnFavoriteMoviesUseCase
import com.example.movieapplicationjetpackcompose.shared.SearchWidgetState
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
class FavoriteViewModelTest {

    private val getFavoriteMoviesFlowUseCase: GetFavoriteMoviesFlowUseCase = mockk()
    private val searchOnFavoriteMoviesUseCase: SearchOnFavoriteMoviesUseCase = mockk()
    private val addMoviesToFavoriteUseCase: AddMoviesToFavoriteUseCase = mockk()
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase = mockk()
    private lateinit var viewModel: FavoriteViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeAll
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FavoriteViewModel(
            getFavoriteMoviesFlowUseCase,
            searchOnFavoriteMoviesUseCase,
            addMoviesToFavoriteUseCase,
            removeMovieFromFavoriteUseCase
        )
    }

    @AfterAll
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `fetchFavoriteMovies should update state with favorite movies`() = runTest {
        // Arrange
        val movies = listOf(Movie(
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
            isFavorite = true
        ))
        coEvery { getFavoriteMoviesFlowUseCase() } returns flowOf(movies)

        viewModel.setState { copy(movies = movies) }
        // Act
        viewModel.setEvent(FavoriteContract.Event.FetchFavoriteMovies)

        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(movies, state.movies)
        Assertions.assertFalse(state.loading)
        coVerify { getFavoriteMoviesFlowUseCase() }
    }

    @Test
    fun `onOpenSearch should open search widget`() = runTest {
        // Act
        viewModel.setEvent(FavoriteContract.Event.OnOpenSearch)
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(SearchWidgetState.OPENED, state.searchWidgetState)
    }

    @Test
    fun `onCloseSearch should close search widget`() = runTest {
        // Arrange
        viewModel.setEvent(FavoriteContract.Event.OnOpenSearch)
        advanceUntilIdle()

        // Act
        viewModel.setEvent(FavoriteContract.Event.OnCloseSearch)
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(SearchWidgetState.CLOSED, state.searchWidgetState)
    }

    @Test
    fun `onSearchQueryChange should update search query`() = runTest {
        // Act
        val query = "New Query"
        viewModel.setEvent(FavoriteContract.Event.OnSearchQueryChange(query))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(query, state.searchQuery)
    }

    @Test
    fun `search should update state with search results`() = runTest {
        // Arrange
        val movies = listOf(Movie(
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
            isFavorite = true
        ))
        coEvery { searchOnFavoriteMoviesUseCase(any()) } returns flowOf(Resource.Success(movies))

        // Act
        viewModel.setEvent(FavoriteContract.Event.OnSearchTriggered("query"))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(movies, state.movies)
        Assertions.assertFalse(state.loading)
        coVerify { searchOnFavoriteMoviesUseCase(any()) }
    }

    @Test
    fun `addToFavorite should update movie as favorite`() = runTest {
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
            isFavorite = true
        )
        coEvery { addMoviesToFavoriteUseCase(any()) } returns flowOf(Resource.Success(Unit))


        viewModel.setState { copy(movies = listOf(movie)) }

        // Act
        viewModel.setEvent(FavoriteContract.Event.AddToFavorite(movie))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertTrue(state.movies?.first()?.isFavorite == true)
        coVerify { addMoviesToFavoriteUseCase(any()) }
    }

    @Test
    fun `removeFromFavorite should update movie as not favorite`() = runTest {
        // Arrange
        val movieId = 1
        val movie = Movie(
            adult = false,
            backdropUrl = "BackdropUrl",
            id = movieId,
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
        coEvery { removeMovieFromFavoriteUseCase(any()) } returns flowOf(Resource.Success(Unit))


        viewModel.setState { copy(movies = listOf(movie)) }

        // Act
        viewModel.setEvent(FavoriteContract.Event.RemoveFromFavorite(movieId))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertTrue(state.movies?.first()?.isFavorite == false)
        coVerify { removeMovieFromFavoriteUseCase(any()) }
    }
}
