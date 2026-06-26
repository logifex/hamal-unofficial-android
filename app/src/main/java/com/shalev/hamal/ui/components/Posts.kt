package com.shalev.hamal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.models.PostUi
import com.shalev.hamal.ui.components.post.PostLayout
import com.shalev.hamal.utils.getCurrentlyPlayingItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun Posts(
    posts: ImmutableList<PostUi>,
    isFocused: Boolean,
    exoPlayer: ExoPlayer,
    listState: LazyListState,
    onPostClick: (String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    onScrollEnd: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    var manuallyPlayedId by remember { mutableStateOf<String?>(null) }
    var currentlyPlayingItemId by remember { mutableStateOf<String?>(null) }
    val updatedPlayingItem by rememberUpdatedState(currentlyPlayingItemId)

    if (isFocused) {
        LaunchedEffect(listState, posts, manuallyPlayedId, exoPlayer) {
            snapshotFlow {
                val layoutInfo = listState.layoutInfo
                getCurrentlyPlayingItem(layoutInfo, posts, manuallyPlayedId)
            }.collect { nextPlayingItemId ->
                if (nextPlayingItemId != updatedPlayingItem) {
                    exoPlayer.run {
                        if (mediaItemCount > 0) {
                            clearMediaItems()
                            stop()
                        }
                    }
                    currentlyPlayingItemId = nextPlayingItemId
                    if (manuallyPlayedId != null && nextPlayingItemId != manuallyPlayedId) {
                        manuallyPlayedId = null
                    }
                }
            }
        }
    }

    LaunchedEffect(listState, onScrollEnd) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount

            if (totalItems > 0) {
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisibleItemIndex >= totalItems - 5
            } else {
                false
            }
        }.collect { isNearEnd ->
            if (isNearEnd) {
                onScrollEnd()
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(
            items = posts,
            key = { post -> post.id },
            contentType = { item -> item.firstMedia?.type ?: "text" }) { post ->
            PostLayout(
                post = post.data,
                displayedBody = post.displayBody,
                galleryItems = post.galleryItems,
                isExpanded = false,
                onPostClick = onPostClick,
                currentlyPlayingMedia = if (isFocused && currentlyPlayingItemId == post.id) post.firstMedia?.id
                else null,
                exoPlayer = exoPlayer,
                onPlayMedia = { _ -> manuallyPlayedId = post.id },
                onVideoFullScreen = onVideoFullScreen,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}