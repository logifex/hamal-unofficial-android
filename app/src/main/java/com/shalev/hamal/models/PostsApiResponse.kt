package com.shalev.hamal.models

import kotlinx.serialization.Serializable

@Serializable
data class PostsApiResponse(
    val data: List<Post>
)
