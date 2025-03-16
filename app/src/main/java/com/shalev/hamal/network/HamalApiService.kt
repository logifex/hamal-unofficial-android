package com.shalev.hamal.network

import com.shalev.hamal.models.PostApiResponse
import com.shalev.hamal.models.PostsApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HamalApiService {
    @GET("item")
    suspend fun getItems(@Query("from") from: Long?): PostsApiResponse

    @GET("item/v2/{id}")
    suspend fun getItem(@Path("id") id: String): PostApiResponse
}
