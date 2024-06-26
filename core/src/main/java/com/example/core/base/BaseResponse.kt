package com.example.core.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val dates:Dates?,
    val page:Int?,
    val results:T,
    @SerializedName("total_pages")
    val totalPage:Int?,
    @SerializedName("total_results")
    val totalResults:Int?
)

data class Dates(
    val maximum:String?,
    val minimum:String?
)
