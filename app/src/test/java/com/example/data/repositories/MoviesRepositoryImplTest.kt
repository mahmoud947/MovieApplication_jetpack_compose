package com.example.data.repositories

import com.example.core.base.BaseResponse
import com.example.data.datasource.local.dao.MovieDao
import com.example.data.datasource.local.entity.MovieEntity
import com.example.data.datasource.remote.dto.MovieDetailsDto
import com.example.data.datasource.remote.dto.MovieDto
import com.example.data.datasource.remote.dto.VideoDto
import com.example.data.datasource.remote.service.MovieService
import com.example.data.mapper.toDomain
import com.example.domain.models.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MoviesRepositoryImplTest {

    private lateinit var repository: MoviesRepositoryImpl
    private val movieService: MovieService = mockk()
    private val movieDao: MovieDao = mockk()

    @BeforeEach
    fun setUp() {
        repository = MoviesRepositoryImpl(movieService, movieDao)
    }

    @Test
    fun `getPopularMovies should return list of movies`() = runBlocking {
        // Arrange
        val movieEntity = MovieEntity(
            id = 1,
            adult = false,
            backdropUrl = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterUrl = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323

        )

        val movie = Movie(
            id = 1,
            adult = false,
            backdropUrl = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterUrl = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323,
            isFavorite = true
        )

        val movieDto = MovieDto(
            id = 1,
            adult = false,
            backdropPath = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterPath = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323,
            genreIds = emptyList()
        )


        val movieEntityList = listOf(movieEntity)
        val movieDtoList = listOf(movieDto)
        val movieList = listOf(movie)
        val movieResponse = BaseResponse<List<MovieDto>>(
            dates = null,
            results = movieDtoList,
            totalResults = 3,
            page = 1,
            totalPage = 1
        )
        coEvery { movieService.getMoviesByCategory(any()) } returns movieResponse
        coEvery { movieDao.isFavorite(any()) } returns null

        // Act
        val result = repository.getPopularMovies()

        // Assert
        assertEquals(movieDtoList.map { it.toDomain(false) }, result)
        coVerify { movieService.getMoviesByCategory(any()) }
        coVerify { movieDao.isFavorite(any()) }
    }


    @Test
    fun `getMovieDetails should return movie details`() = runBlocking {
        // Arrange
        val movieDetailsEntity = MovieDetailsDto(
            adult = false,
            backdropPath = "backdropPath",
            belongsToCollection = "belongsToCollection",
            budget = 3,
            genres = listOf(),
            homepage = "sdf",
            id = 323,
            imdbId = "32",
            originCountry = listOf(),
            originalLanguage = "en",
            originalTitle = "The",
            overview = "aaaaaaa",
            popularity = 23.3,
            posterPath = "posterPath",
            productionCompanies = listOf(),
            productionCountries = listOf(),
            releaseDate = "22/22/2040",
            revenue = 11,
            runtime = 123,
            spokenLanguages = listOf(),
            status = "status",
            tagline = "taglin",
            title = "title",
            video = false,
            voteAverage = 22.2,
            voteCount = 11
        )
        coEvery { movieService.getMovieDetails(any()) } returns movieDetailsEntity

        // Act
        val result = repository.getMovieDetails(1)

        // Assert
        assertEquals(movieDetailsEntity.toDomain(), result)
        coVerify { movieService.getMovieDetails(any()) }
    }

    @Test
    fun `getMovieVideo should return list of movie videos`() = runBlocking {
        // Arrange
        val movieVideoDto = VideoDto(
            id ="Key",
            iso31661 ="iso",
            iso6391 ="iso",
            key ="Key",
            name ="name",
            official =false,
            publishedAt ="22/22/2222",
            site ="site",
            size =12,
            type ="video"
        )
        val movieVideoDtoList = listOf(movieVideoDto)
        val movieVideoResponse = BaseResponse<List<VideoDto>>(
            dates = null,
            results = movieVideoDtoList,
            totalResults = 3,
            page = 1,
            totalPage = 1
        )
        coEvery { movieService.getMovieVideos(any()) } returns movieVideoResponse

        // Act
        val result = repository.getMovieVideo(1)

        // Assert
        assertEquals(movieVideoDtoList.map { it.toDomain() }, result)
        coVerify { movieService.getMovieVideos(any()) }
    }

    @Test
    fun `search should return list of movies`() = runBlocking {
        // Arrange
        val movieDto = MovieDto(
            id = 1,
            adult = false,
            backdropPath = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterPath = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323,
            genreIds = emptyList()
        )

        val movieDtoList = listOf(movieDto)
        val movieResponse = BaseResponse<List<MovieDto>>(
            dates = null,
            results = movieDtoList,
            totalResults = 3,
            page = 1,
            totalPage = 1
        )
        coEvery { movieService.search(any()) } returns movieResponse
        coEvery { movieDao.isFavorite(any()) } returns null

        // Act
        val result = repository.search("query")

        // Assert
        assertEquals(movieDtoList.map { it.toDomain(false) }, result)
        coVerify { movieService.search(any()) }
        coVerify { movieDao.isFavorite(any()) }
    }

    @Test
    fun `searchOnFavorite should return list of favorite movies`() = runBlocking {
        // Arrange
        val movieEntity = MovieEntity(
            id = 1,
            adult = false,
            backdropUrl = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterUrl = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323

        )
        val movieList = listOf(movieEntity)
        coEvery { movieDao.searchMoviesByTitle(any()) } returns movieList

        // Act
        val result = repository.searchOnFavorite("query")

        // Assert
        assertEquals(movieList.map { it.toDomain() }, result)
        coVerify { movieDao.searchMoviesByTitle(any()) }
    }

    @Test
    fun `getFavoriteMovies should return list of favorite movies`() = runBlocking {
        // Arrange
        val movieEntity = MovieEntity(
            id = 1,
            adult = false,
            backdropUrl = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterUrl = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323

        )
        val movieList = listOf(movieEntity)
        coEvery { movieDao.getFavoriteMovies() } returns movieList

        // Act
        val result = repository.getFavoriteMovies()

        // Assert
        assertEquals(movieList.map { it.toDomain() }, result)
        coVerify { movieDao.getFavoriteMovies() }
    }

    @Test
    fun `getFavoriteMoviesFlow should return flow of favorite movies`() = runBlocking {
        // Arrange
        val movieEntity = MovieEntity(
            id = 1,
            adult = false,
            backdropUrl = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterUrl = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323

        )
        val movieList = listOf(movieEntity)
        coEvery { movieDao.getFavoriteMoviesFlow() } returns flowOf(movieList)

        // Act
        val result = repository.getFavoriteMoviesFlow().toList()

        // Assert
        assertEquals(listOf(movieList.map { it.toDomain() }), result)
        coVerify { movieDao.getFavoriteMoviesFlow() }
    }

    @Test
    fun `addToFavorite should add movie to favorites`() = runBlocking {
        // Arrange

        val movie = Movie(
            id = 1,
            adult = false,
            backdropUrl = "backdropUrl",
            originalLanguage = "originalLanguage",
            originalTitle = "originalTitle",
            overview = "overview",
            popularity = 0.3,
            posterUrl = "posterUrl",
            releaseDate = "releaseDate",
            title = "title",
            video = false,
            voteAverage = 33.0,
            voteCount = 323,
            isFavorite = true
        )

        coEvery { movieDao.addToFavorite(any()) } returns Unit

        // Act
        repository.addToFavorite(movie)

        // Assert
        coVerify { movieDao.addToFavorite(any()) }
    }

    @Test
    fun `isFavoriteMovie should return true if movie is favorite`() = runBlocking {
        // Arrange
        coEvery { movieDao.isFavorite(any()) } returns 1

        // Act
        val result = repository.isFavoriteMovie(1)

        // Assert
        assertTrue(result)
        coVerify { movieDao.isFavorite(any()) }
    }

    @Test
    fun `isFavoriteMovieFlow should return flow of true if movie is favorite`() = runBlocking {
        // Arrange
        coEvery { movieDao.isFavoriteFlow(any()) } returns flowOf(1)

        // Act
        val result = repository.isFavoriteMovieFlow(1).toList()

        // Assert
        assertEquals(listOf(true), result)
        coVerify { movieDao.isFavoriteFlow(any()) }
    }

    @Test
    fun `removeFromFavorite should remove movie from favorites`() = runBlocking {
        // Arrange
        coEvery { movieDao.removeFromFavorite(any()) } returns 1

        // Act
        repository.removeFromFavorite(1)

        // Assert
        coVerify { movieDao.removeFromFavorite(any()) }
    }
}
