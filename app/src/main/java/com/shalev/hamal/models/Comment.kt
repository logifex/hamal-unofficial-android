package com.shalev.hamal.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    @SerialName(value = "_id")
    val id: String,
    val text: String,
    val user: User,
    val likes: Likes,
    val createdAt: Long,
    val replies: List<Comment>?
)
