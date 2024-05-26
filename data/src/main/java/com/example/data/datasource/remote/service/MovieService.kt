package com.example.data.datasource.remote.service

import com.example.core.base.BaseResponse
import com.example.data.datasource.remote.dto.MovieDetailsDto
import com.example.data.datasource.remote.dto.MovieDto
import com.example.data.datasource.remote.dto.VideoDto
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

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): MovieDetailsDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): BaseResponse<List<VideoDto>>

}