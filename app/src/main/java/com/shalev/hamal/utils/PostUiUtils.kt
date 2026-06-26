package com.shalev.hamal.utils

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import com.shalev.hamal.models.PostBodyUi
import com.shalev.hamal.models.PostUi
import kotlin.math.abs

fun parsePostBodyHtml(
    body: List<PostBodyUi>,
    linkColor: Color,
    maxLength: Int
): Pair<Map<String, AnnotatedString>, Boolean> {
    var length = 0
    var hasMore = false
    val map = buildMap<String, AnnotatedString> {
        for (content in body) {
            if (content !is PostBodyUi.Text) continue
            if (length >= maxLength) {
                hasMore = true
                break
            }

            val annotated = AnnotatedString.fromHtml(
                htmlString = content.value.replace("\n", "<br>"),
                linkStyles = TextLinkStyles(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = linkColor
                    )
                )
            )

            if (length + annotated.length > maxLength) {
                hasMore = true
                put(content.id, annotated.subSequence(0, maxLength - length).trim())
                break
            } else {
                put(content.id, annotated.trim())
                length += annotated.length
            }
        }
    }

    return Pair(map, hasMore)
}

fun getCurrentlyPlayingItem(
    layoutInfo: LazyListLayoutInfo,
    posts: List<PostUi>,
    manuallyPlayedVideoId: String?
): String? {
    val midPoint = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

    var minDistance = Int.MAX_VALUE
    var closestItemId: String? = null

    for (item in layoutInfo.visibleItemsInfo) {
        val post = posts.getOrNull(item.index) ?: continue

        if (post.id == manuallyPlayedVideoId) {
            return post.id
        }

        if (post.firstMedia !is PostBodyUi.Video) continue

        val distance = abs(item.offset + item.size / 2 - midPoint)
        if (distance < minDistance) {
            minDistance = distance
            closestItemId = post.id
        }
    }

    return closestItemId
}