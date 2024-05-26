package com.example.data.mapper

import com.example.data.datasource.local.entity.MovieEntity
import com.example.data.datasource.remote.dto.MovieDetailsDto
import com.example.data.datasource.remote.dto.MovieDto
import com.example.data.datasource.remote.dto.VideoDto
import com.example.domain.models.Movie
import com.example.domain.models.MovieDetails
import com.example.domain.models.MovieVideo


fun MovieDto.toDomain(isFavorite: Boolean): Movie = Movie(
    adult = adult ?: false,
    backdropUrl = backdropUrl,
    id = id ?: 0,
    originalLanguage = originalLanguage.orEmpty(),
    originalTitle = originalTitle.orEmpty(),
    overview = overview.orEmpty(),
    popularity = popularity ?: 0.0,
    posterUrl = posterUrl.orEmpty(),
    releaseDate = releaseDate.orEmpty(),
    title = title.orEmpty(),
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    isFavorite = isFavorite
)

fun VideoDto.toDomain(): MovieVideo = MovieVideo(
    id = id,
    iso31661 = iso31661,
    iso6391 = iso6391,
    key = key,
    name = name,
    official = official,
    publishedAt = publishedAt,
    site = site,
    size = size,
    type = type
)

fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = id ?: 0,
    adult = adult,
    backdropUrl = backdropUrl,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
)

fun MovieEntity.toDomain(): Movie = Movie(
    adult = adult ?: false,
    backdropUrl = backdropUrl.orEmpty(),
    id = id,
    originalLanguage = originalLanguage.orEmpty(),
    originalTitle = originalTitle.orEmpty(),
    overview = overview.orEmpty(),
    popularity = popularity ?: 0.0,
    posterUrl = posterUrl.orEmpty(),
    releaseDate = releaseDate.orEmpty(),
    title = title.orEmpty(),
    video = video ?: false,
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    isFavorite = true
)


fun MovieDetailsDto.GenreDto.toDomain(): MovieDetails.Genre = MovieDetails.Genre(
    id = id,
    name = name,
)


fun MovieDetailsDto.ProductionCompanyDto.toDomain(): MovieDetails.ProductionCompany =
    MovieDetails.ProductionCompany(
        id = id, logoPath = logoPath, name = name, originCountry = originCountry
    )

fun MovieDetailsDto.ProductionCountryDto.toDomain(): MovieDetails.ProductionCountry =
    MovieDetails.ProductionCountry(
        iso31661 = iso31661, name = name
    )

fun MovieDetailsDto.SpokenLanguageDto.toDomain(): MovieDetails.SpokenLanguage =
    MovieDetails.SpokenLanguage(
        englishName = englishName, iso6391 = iso6391, name = name

    )

fun MovieDetailsDto.toDomain(): MovieDetails = MovieDetails(
    adult = adult,
    backdropUrl = backdropUrl,
    belongsToCollection = belongsToCollection,
    budget = budget,
    genres = genres?.map { it.toDomain() },
    homepage = homepage,
    id = id,
    imdbId = imdbId,
    originCountry = originCountry,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterUrl = posterUrl,
    productionCompanies = productionCompanies?.map { it.toDomain() },
    productionCountries = productionCountries?.map { it.toDomain() },
    releaseDate = releaseDate,
    revenue = revenue,
    runtime = runtime,
    spokenLanguages = spokenLanguages?.map { it.toDomain() },
    status = status,
    tagline = tagline,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)