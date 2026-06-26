package com.shalev.hamal.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Comment(
    @SerialName(value = "_id")
    val id: String,
    val text: String,
    val user: User,
    val likes: Likes,
    val createdAt: Long,
    val replies: List<Comment>,
    val isReply: Boolean
)

fun List<Comment>.flatten(): List<Comment> {
    return buildList {
        for (comment in this@flatten) {
            add(comment)
            addAll(comment.replies)
        }
    }
}
