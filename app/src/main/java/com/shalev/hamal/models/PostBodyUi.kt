package com.shalev.hamal.models

import com.shalev.hamal.utils.extractAspectRatioFromUrl
import com.shalev.hamal.utils.extractSizeFromEmbed

sealed class PostBodyUi {
    abstract val id: String
    abstract val type: String

    data class Title(override val id: String, override val type: String, val value: String) :
        PostBodyUi()

    data class Text(override val id: String, override val type: String, val value: String) :
        PostBodyUi()

    data class Picture(
        override val id: String,
        override val type: String,
        val value: String,
        val aspectRatio: Float
    ) : PostBodyUi()

    data class Embed(
        override val id: String,
        override val type: String,
        val value: String,
        val dimensions: Dimensions
    ) : PostBodyUi()

    data class Gallery(
        override val id: String,
        override val type: String,
        val value: String,
        val aspectRatio: Float
    ) : PostBodyUi()

    data class Video(
        override val id: String,
        override val type: String,
        val value: VideoValue,
        val aspectRatio: Float
    ) : PostBodyUi()

    data class Podcast(override val id: String, override val type: String, val value: String) :
        PostBodyUi()

    data class Unimplemented(
        override val id: String,
        override val type: String,
        val value: String
    ) : PostBodyUi()
}

fun PostBody.toPostBodyUi(): PostBodyUi {
    when (this) {
        is PostBody.Title -> return PostBodyUi.Title(id, type, value)
        is PostBody.Text -> return PostBodyUi.Text(id, type, value)
        is PostBody.Picture -> {
            val aspectRatio = extractAspectRatioFromUrl(value)
            return PostBodyUi.Picture(id, type, value, aspectRatio)
        }

        is PostBody.Embed -> {
            val dimensions = extractSizeFromEmbed(value)
            return PostBodyUi.Embed(id, type, value, dimensions)
        }

        is PostBody.Gallery -> {
            val aspectRatio = extractAspectRatioFromUrl(value)
            return PostBodyUi.Gallery(id, type, value, aspectRatio)
        }

        is PostBody.Video -> {
            val aspectRatio = extractAspectRatioFromUrl(value.url)
            return PostBodyUi.Video(id, type, value, aspectRatio)
        }

        is PostBody.Podcast -> return PostBodyUi.Podcast(id, type, value)

        else -> return PostBodyUi.Unimplemented(id, type, value.toString())
    }
}
