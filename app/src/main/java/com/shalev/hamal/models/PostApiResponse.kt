package com.shalev.hamal.models

import kotlinx.serialization.Serializable

@Serializable
data class PostApiResponse(
    val item: Post
)

