package com.shalev.hamal.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
sealed class PostBody {
    @SerialName(value = "_id")
    abstract val id: String
    abstract val type: String
    abstract val value: Any

    @Serializable
    @SerialName("title")
    data class Title(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: String
    ) : PostBody()

    @Serializable
    @SerialName("text")
    data class Text(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: String
    ) : PostBody()

    @Serializable
    @SerialName("picture")
    data class Picture(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: String
    ) : PostBody()

    @Serializable
    @SerialName("embed")
    data class Embed(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: String
    ) : PostBody()

    @Serializable
    @SerialName("gallery")
    data class Gallery(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: String
    ) : PostBody()

    @Serializable
    @SerialName("video")
    data class Video(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: VideoValue
    ) : PostBody()

    @Serializable
    @SerialName("podcast")
    data class Podcast(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: String
    ) : PostBody()

    @Serializable
    @SerialName("vote")
    data class Vote(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: JsonElement
    ) : PostBody()

    @Serializable
    @SerialName("repost")
    data class Repost(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: JsonElement
    ) : PostBody()

    @Serializable
    @SerialName("battle")
    data class Battle(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: JsonElement
    ) : PostBody()

    @Serializable
    @SerialName("pin")
    data class Pin(
        @SerialName(value = "_id") override val id: String,
        override val type: String,
        override val value: JsonElement
    ) : PostBody()
}
