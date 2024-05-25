package com.example.data.datasource.remote.service

import com.example.core.base.BaseResponse
import com.example.data.datasource.remote.dto.MovieDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/{category}")
    suspend fun getMoviesByCategory(
        @Path("category")
        category:String
    ): BaseResponse<List<MovieDto>>

    @GET("search/movie")
    suspend fun search(
        @Query("query")
        query:String
    ): BaseResponse<List<MovieDto>>
}