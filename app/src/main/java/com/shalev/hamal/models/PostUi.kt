package com.shalev.hamal.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class PostUi(
    val data: Post,
    val displayBody: ImmutableList<PostBodyUi>,
    val galleryItems: ImmutableList<PostBodyUi.Gallery>,
    val firstMedia: PostBodyUi? = null
) {
    val id get() = data.id
}

fun Post.toPostUiItem(): PostUi {
    val displayBody = mutableListOf<PostBodyUi>()
    val gallery = mutableListOf<PostBodyUi.Gallery>()

    for (item in body) {
        val postBodyUi = item.toPostBodyUi()
        displayBody.add(postBodyUi)
        if (postBodyUi is PostBodyUi.Gallery) {
            gallery.add(postBodyUi)
        }
    }

    return PostUi(this, displayBody.toImmutableList(), gallery.toImmutableList())
}

fun Post.toPostFeedItem(): PostUi {
    val texts = mutableListOf<PostBodyUi>()
    val gallery = mutableListOf<PostBodyUi.Gallery>()
    var firstMedia: PostBodyUi? = null

    for (item in this.body) {
        if (item is PostBody.Title || item is PostBody.Text) {
            texts.add(item.toPostBodyUi())
        } else if (firstMedia == null) {
            val postBodyUi = item.toPostBodyUi()
            firstMedia = postBodyUi
            if (postBodyUi is PostBodyUi.Gallery) {
                gallery.add(postBodyUi)
            }
        } else if (firstMedia is PostBodyUi.Gallery && item is PostBody.Gallery) {
            gallery.add(item.toPostBodyUi() as PostBodyUi.Gallery)
        }
    }

    return PostUi(
        this,
        (if (firstMedia != null) (texts + firstMedia) else texts).toImmutableList(),
        gallery.toImmutableList(),
        firstMedia
    )
}
