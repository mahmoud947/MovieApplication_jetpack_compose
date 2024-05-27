package com.example.movieapplicationjetpackcompose.ui.screens.home

import com.example.core.utils.Resource
import com.example.domain.models.Movie
import com.example.domain.usecase.AddMoviesToFavoriteUseCase
import com.example.domain.usecase.GetPopularMoviesUseCase
import com.example.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.example.domain.usecase.SearchMoviesUseCase
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
class HomeViewModelTest {

    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk()
    private val searchMoviesUseCase: SearchMoviesUseCase = mockk()
    private val addMoviesToFavoriteUseCase: AddMoviesToFavoriteUseCase = mockk()
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase = mockk()
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeAll
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            getPopularMoviesUseCase,
            searchMoviesUseCase,
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
    fun `fetchMovies should update state with movies`() = runTest {
        // Arrange
        val movies = listOf(Movie(
            adult = true,
            backdropUrl = "September",
            id = 6222,
            originalLanguage = "Antionette",
            originalTitle = "Quenton",
            overview = "Khoa",
            popularity = 66.376,
            posterUrl = "Umar",
            releaseDate = "Julieanna",
            title = "Brande",
            video = true,
            voteAverage = 13.859,
            voteCount = 4628,
            isFavorite = true
        ))
        coEvery { getPopularMoviesUseCase() } returns flowOf(Resource.Success(movies))

        viewModel.setState { copy(movies = movies) }
        // Act
        viewModel.setEvent(HomeContract.Event.FetchMovies)
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(movies, state.movies)
        Assertions.assertFalse(state.loading)
        coVerify { getPopularMoviesUseCase() }
    }

    @Test
    fun `onOpenSearch should open search widget`() = runTest {
        // Act
        viewModel.setEvent(HomeContract.Event.OnOpenSearch)
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(SearchWidgetState.OPENED, state.searchWidgetState)
    }

    @Test
    fun `onCloseSearch should close search widget`() = runTest {
        // Arrange
        viewModel.setEvent(HomeContract.Event.OnOpenSearch)
        advanceUntilIdle()

        // Act
        viewModel.setEvent(HomeContract.Event.OnCloseSearch)
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(SearchWidgetState.CLOSED, state.searchWidgetState)
    }

    @Test
    fun `onSearchQueryChange should update search query`() = runTest {
        // Act
        val query = "New Query"
        viewModel.setEvent(HomeContract.Event.OnSearchQueryChange(query))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(query, state.searchQuery)
    }

    @Test
    fun `search should update state with search results`() = runTest {
        // Arrange
        val movies = listOf(Movie(
            adult = true,
            backdropUrl = "Ivania",
            id = 6331,
            originalLanguage = "Otha",
            originalTitle = "Merrick",
            overview = "Shenika",
            popularity = 52.426,
            posterUrl = "Moira",
            releaseDate = "Kela",
            title = "Rebeccah",
            video = false,
            voteAverage = 14.412,
            voteCount = 3014,
            isFavorite = true
        ))
        coEvery { searchMoviesUseCase(any()) } returns flowOf(Resource.Success(movies))

        viewModel.setState { copy(movies = movies) }
        // Act
        viewModel.setEvent(HomeContract.Event.OnSearchTriggered("query"))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertEquals(movies, state.movies)
        Assertions.assertFalse(state.loading)
        coVerify { searchMoviesUseCase(any()) }
    }
    @Test
    fun `addToFavorite should update movie as favorite`() = runTest {
        // Arrange
        val movie = Movie(
            adult = false,
            backdropUrl = "Mahmoud",
            id = 8235,
            originalLanguage = "Kawana",
            originalTitle = "Jabari",
            overview = "Chelsie",
            popularity = 88.250,
            posterUrl = "Kathrin",
            releaseDate = "Winfred",
            title = "Janel",
            video = true,
            voteAverage = 34.925,
            voteCount = 5503,
            isFavorite = true
        )
        coEvery { addMoviesToFavoriteUseCase(any()) } returns flowOf(Resource.Success(Unit))

        viewModel.setState { copy(movies = listOf(movie)) }

        // Act
        viewModel.setEvent(HomeContract.Event.AddToFavorite(movie))
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
            backdropUrl = "Isaura",
            id = movieId,
            originalLanguage = "Gisell",
            originalTitle = "Janmichael",
            overview = "Porter",
            popularity = 41.444,
            posterUrl = "Laken",
            releaseDate = "Jousha",
            title = "Bethanie",
            video = true,
            voteAverage = 33.982,
            voteCount = 7561,
            isFavorite = false
        )
        viewModel.setState { copy(movies = listOf(movie)) }
        coEvery { removeMovieFromFavoriteUseCase(any()) } returns flowOf(Resource.Success(Unit))

        // Act
        viewModel.setEvent(HomeContract.Event.RemoveFromFavorite(movieId))
        advanceUntilIdle()

        // Assert
        val state = viewModel.viewState.value
        Assertions.assertTrue(state.movies?.first()?.isFavorite == false)
        coVerify { removeMovieFromFavoriteUseCase(any()) }
    }
}
