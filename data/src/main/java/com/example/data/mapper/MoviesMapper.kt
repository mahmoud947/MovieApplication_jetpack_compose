package com.example.data.mapper

import com.example.data.datasource.local.entity.MovieEntity
import com.example.data.datasource.remote.dto.MovieDto
import com.example.domain.models.Movie


fun MovieDto.toDomain(isFavorite:Boolean): Movie = Movie(
    adult = adult ?: false,
    backdropUrl = backdropUrl,
    genreIds = genreIds.orEmpty(),
    id = id?:0,
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

fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = id ?: 0,
    adult = adult,
    backdropUrl = backdropUrl,
    genreIds = genreIds,
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
    genreIds = genreIds,
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