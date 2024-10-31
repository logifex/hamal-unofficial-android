package com.shalev.hamal.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName(value = "_id")
    val id: String,
    @SerialName(value = "display_name")
    val displayName: String,
    val avatar: String
)
