package com.shalev.hamal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.models.Post
import com.shalev.hamal.models.PostBody
import com.shalev.hamal.ui.components.post.PostLayout
import kotlin.math.abs

@Composable
fun Posts(
    posts: List<Post>,
    isFocused: Boolean,
    exoPlayer: ExoPlayer,
    listState: LazyListState,
    onPostClick: (String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    onScrollEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var manuallyPlayedVideoId by remember { mutableStateOf<String?>(null) }
    var currentlyPlayingItem by remember { mutableStateOf<Pair<String, PostBody>?>(null) }

    LaunchedEffect(listState, posts, manuallyPlayedVideoId, isFocused, exoPlayer) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val nextPlayingItem =
                    if (isFocused) {
                        getCurrentlyPlayingItem(layoutInfo, posts, manuallyPlayedVideoId)
                    } else null

                if (nextPlayingItem != currentlyPlayingItem) {
                    exoPlayer.run {
                        if (mediaItemCount > 0) {
                            clearMediaItems()
                            stop()
                        }
                    }
                }
                currentlyPlayingItem = nextPlayingItem
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount

                if (totalItems > 0) {
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                    if (lastVisibleItemIndex >= totalItems - 5) {
                        onScrollEnd()
                    }
                }
            }
    }

    LazyColumn(state = listState, modifier = modifier) {
        items(items = posts, key = { post -> post.id }) { post ->
            Column {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                PostLayout(
                    post = post,
                    isExpanded = false,
                    onPostClick = onPostClick,
                    currentlyPlayingMedia =
                    if (currentlyPlayingItem?.first == post.id) currentlyPlayingItem?.second?.id
                    else null,
                    exoPlayer = exoPlayer,
                    onPlayMedia = { manuallyPlayedVideoId = it },
                    onVideoFullScreen = onVideoFullScreen,
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
        }
    }
}

private fun getCurrentlyPlayingItem(
    layoutInfo: LazyListLayoutInfo,
    posts: List<Post>,
    manuallyPlayedVideoId: String?
): Pair<String, PostBody>? {
    val midPoint = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    val itemsFromCenter =
        layoutInfo.visibleItemsInfo.sortedBy { abs((it.offset + it.size / 2) - midPoint) }

    val visiblePlayableMedia = itemsFromCenter.mapNotNull { item ->
        val post = posts[item.index]
        val firstMedia = post.body.firstOrNull { it !is PostBody.Title && it !is PostBody.Text }
        firstMedia.takeIf { (firstMedia is PostBody.Video || firstMedia is PostBody.Podcast) }
            ?.let { media ->
                post.id to media
            }
    }

    return visiblePlayableMedia.find { it.second.id == manuallyPlayedVideoId }
        ?: visiblePlayableMedia.filterIsInstance<Pair<String, PostBody.Video>>().firstOrNull()
}