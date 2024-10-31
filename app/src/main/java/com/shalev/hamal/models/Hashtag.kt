package com.shalev.hamal.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hashtag(
    @SerialName(value = "_id")
    val id: String,
    val text: String
)
