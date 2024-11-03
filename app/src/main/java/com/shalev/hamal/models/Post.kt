package com.shalev.hamal.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName(value = "_id")
    val id: String,
    val active: Boolean,
    val body: List<PostBody>,
    val publishedAt: Long,
    val writer: User,
    @SerialName(value = "comments_count")
    val commentsCount: Int,
    val comments: List<Comment>?,
    val likes: Likes,
    val metaData: PostMetadata,
    val hashtags: List<Hashtag>,
    val stampData: PostStampData
)