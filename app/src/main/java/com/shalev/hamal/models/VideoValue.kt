package com.shalev.hamal.models

import kotlinx.serialization.Serializable

@Serializable
data class VideoValue(
    val previewUrl: String?,
    val url: String
)
