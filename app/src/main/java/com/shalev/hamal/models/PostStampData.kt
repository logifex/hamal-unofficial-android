package com.shalev.hamal.models

import kotlinx.serialization.Serializable

@Serializable
data class PostStampData(
    val text: String,
    val color: String
)
