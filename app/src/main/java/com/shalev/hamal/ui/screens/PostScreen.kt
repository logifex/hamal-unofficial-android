package com.shalev.hamal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.data.PostUiState
import com.shalev.hamal.ui.components.comment.CommentCount
import com.shalev.hamal.ui.components.post.PostLayout
import com.shalev.hamal.ui.components.comment.CommentThread
import com.shalev.hamal.ui.components.LoadingIndicator
import com.shalev.hamal.ui.components.Message

@Composable
fun PostScreen(
    id: String,
    isFocused: Boolean,
    exoPlayer: ExoPlayer,
    onPictureClick: (url: String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    onDeactivate: () -> Unit,
    postViewModel: PostViewModel = viewModel(factory = PostViewModel.Factory(id))
) {
    val postUiState = postViewModel.uiState.collectAsState()
    var currentlyPlayedMedia by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    LaunchedEffect(postUiState.value) {
        if (postUiState.value is PostUiState.Success
            && !(postUiState.value as PostUiState.Success).post.active
        ) {
            onDeactivate()
        }
    }

    LaunchedEffect(listState, exoPlayer) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (index != 0) {
                    exoPlayer.run {
                        if (mediaItemCount > 0) {
                            stop()
                            clearMediaItems()
                        }
                    }
                }
            }
    }

    when (postUiState.value) {
        is PostUiState.Loading -> LoadingIndicator(modifier = Modifier.fillMaxSize())
        is PostUiState.Error -> Message(
            text = stringResource(R.string.fetching_error),
            modifier = Modifier.fillMaxSize()
        )

        is PostUiState.Success -> {
            val post = (postUiState.value as PostUiState.Success).post
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small)
                ),
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_minimal))
            ) {
                item {
                    Column {
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                        PostLayout(
                            post = post,
                            isExpanded = true,
                            onPictureClick = onPictureClick,
                            exoPlayer = exoPlayer,
                            currentlyPlayingMedia = currentlyPlayedMedia.takeIf { isFocused },
                            onPlayMedia = { currentlyPlayedMedia = it },
                            onVideoFullScreen = onVideoFullScreen,
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))
                        CommentCount(
                            count = post.commentsCount,
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
                post.comments?.let {
                    items(items = post.comments.reversed()) { comment ->
                        CommentThread(comment)
                    }
                }
            }
        }
    }
}