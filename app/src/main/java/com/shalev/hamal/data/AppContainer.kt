package com.shalev.hamal.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.shalev.hamal.network.HamalApiService
import com.shalev.hamal.utils.Constants
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val postRepository: PostRepository
}

class DefaultAppContainer : AppContainer {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.API_URL)
        .addConverterFactory(JsonProvider.json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: HamalApiService by lazy {
        retrofit.create(HamalApiService::class.java)
    }

    override val postRepository: PostRepository by lazy {
        NetworkPostRepository(retrofitService)
    }
}