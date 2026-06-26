package com.shalev.hamal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shalev.hamal.R
import com.shalev.hamal.data.PostUiState
import com.shalev.hamal.models.FetchingError
import com.shalev.hamal.ui.AppBar
import com.shalev.hamal.ui.components.comment.CommentCount
import com.shalev.hamal.ui.components.post.PostLayout
import com.shalev.hamal.ui.components.LoadingIndicator
import com.shalev.hamal.ui.components.Message
import com.shalev.hamal.ui.components.comment.CommentLayout
import com.shalev.hamal.ui.providers.LocalExoPlayer
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun PostScreen(
    id: String?,
    slug: String?,
    isFocused: Boolean,
    onPictureClick: (url: String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    onDeactivate: () -> Unit,
    onBackClick: () -> Unit,
    postViewModel: PostViewModel = viewModel(factory = PostViewModel.Factory(id, slug))
) {
    val postUiState = postViewModel.uiState.collectAsState()
    var currentlyPlayedMedia by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val exoPlayer = LocalExoPlayer.current

    LaunchedEffect(postUiState.value, onDeactivate) {
        if (postUiState.value is PostUiState.Success && !(postUiState.value as PostUiState.Success).post.data.active) {
            onDeactivate()
        }
    }

    LaunchedEffect(listState, exoPlayer) {
        snapshotFlow { listState.firstVisibleItemIndex != 0 }
            .collect { isNotTop ->
                if (isNotTop) {
                    exoPlayer.run {
                        if (mediaItemCount > 0) {
                            stop()
                            clearMediaItems()
                        }
                    }
                }
            }
    }

    Scaffold(topBar = {
        AppBar(
            title = stringResource(R.string.post_title),
            navigateUp = onBackClick,
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (postUiState.value) {
            is PostUiState.Loading -> LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            is PostUiState.Error -> {
                val state = postUiState.value as PostUiState.Error
                Message(
                    text = when (state.error) {
                        is FetchingError.NetworkError -> stringResource(R.string.network_error)
                        is FetchingError.HttpError -> stringResource(
                            R.string.http_error, state.error.code
                        )
                    }, modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is PostUiState.Success -> {
                val post = (postUiState.value as PostUiState.Success).post
                LazyColumn(
                    state = listState, verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.padding_small)
                    ), modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.padding_small),
                        start = dimensionResource(R.dimen.padding_minimal),
                        end = dimensionResource(R.dimen.padding_minimal)
                    ), contentPadding = innerPadding
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large))) {
                            PostLayout(
                                post = post.data,
                                displayedBody = post.displayBody.toImmutableList(),
                                galleryItems = persistentListOf(),
                                isExpanded = true,
                                onPictureClick = onPictureClick,
                                exoPlayer = exoPlayer,
                                currentlyPlayingMedia = currentlyPlayedMedia.takeIf { isFocused },
                                onPlayMedia = { id -> currentlyPlayedMedia = id },
                                onVideoFullScreen = onVideoFullScreen,
                            )
                            CommentCount(
                                count = post.data.commentsCount,
                                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                            )
                        }
                    }
                    (postUiState.value as PostUiState.Success).flattenedComments?.let { flattenedComments ->
                        itemsIndexed(
                            items = flattenedComments,
                            key = { _, comment -> comment.id },
                            contentType = { _, comment -> comment.isReply }) { index, comment ->
                            val isNextReply =
                                index < flattenedComments.lastIndex && flattenedComments[index + 1].isReply
                            val indentPadding = dimensionResource(R.dimen.padding_medium)
                            CommentLayout(
                                comment,
                                Modifier.padding(start = if (comment.isReply) indentPadding else 0.dp)
                            )
                            HorizontalDivider(
                                thickness = if (!isNextReply) dimensionResource(R.dimen.border_medium) else dimensionResource(
                                    R.dimen.border_thin
                                ),
                                color = MaterialTheme.colorScheme.inverseSurface,
                                modifier = Modifier.padding(
                                    top = dimensionResource(R.dimen.padding_small),
                                    start = if (isNextReply) indentPadding else 0.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}