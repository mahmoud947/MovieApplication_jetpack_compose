package com.example.data.datasource.remote.service

import com.example.core.base.BaseResponse
import com.example.data.datasource.remote.dto.MovieRes
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("movie/{category}")
    suspend fun getMoviesByCategory(
        @Path("category")
        category:String
    ): BaseResponse<List<MovieRes>>
}