package com.shalev.hamal.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class PostsApiResponse(
    val data: List<Post>
)
