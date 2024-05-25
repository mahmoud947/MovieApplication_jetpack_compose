package com.example.data.mapper

import com.example.data.datasource.remote.dto.MovieDto
import com.example.domain.models.Movie


fun MovieDto.toDomain(): Movie = Movie(
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
    voteCount = voteCount ?: 0

)