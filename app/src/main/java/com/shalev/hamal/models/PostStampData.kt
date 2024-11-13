package com.shalev.hamal.models

import kotlinx.serialization.Serializable

@Serializable
data class PostStampData(
    val isActive: Boolean,
    val text: String,
    val color: String
)
