package com.shalev.hamal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.data.HomeUiState
import com.shalev.hamal.ui.components.Posts
import com.shalev.hamal.ui.components.PostsNotification
import com.shalev.hamal.ui.components.LoadingIndicator
import com.shalev.hamal.ui.components.Message
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isFocused: Boolean,
    exoPlayer: ExoPlayer,
    listState: LazyListState,
    onPostClick: (String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    homeViewModel: HomePostsViewModel = viewModel(factory = HomePostsViewModel.Factory)
) {
    val homeUiState = homeViewModel.uiState.collectAsState()
    val isRefreshing = homeViewModel.refreshing
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(homeUiState.value) {
        if (homeUiState.value is HomeUiState.Loading) {
            listState.scrollToItem(0)
        }
    }

    if (homeUiState.value is HomeUiState.Success) {
        val newPosts = (homeUiState.value as HomeUiState.Success).newPosts

        if (newPosts.isNotEmpty()) {
            LaunchedEffect(newPosts) {
                if (listState.firstVisibleItemIndex <= 1 && listState.firstVisibleItemScrollOffset <= 0) {
                    listState.scrollToItem(0)
                    homeViewModel.resetNewPosts()
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
                PostsNotification(
                    posts = newPosts,
                    onClick = {
                        homeViewModel.resetNewPosts()
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing.value,
        onRefresh = {
            if (homeUiState.value !is HomeUiState.Loading) {
                isRefreshing.value = true
                homeViewModel.getPosts()
            }
        }
    ) {
        when (homeUiState.value) {
            is HomeUiState.Loading -> LoadingIndicator(modifier = Modifier.fillMaxSize())
            is HomeUiState.Error -> Message(
                stringResource(R.string.fetching_error),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )

            is HomeUiState.Success -> {
                val state = homeUiState.value as HomeUiState.Success
                Posts(
                    posts = state.posts,
                    isFocused = isFocused,
                    exoPlayer = exoPlayer,
                    listState = listState,
                    onPostClick = onPostClick,
                    onScrollEnd = { homeViewModel.getMorePosts() },
                    onVideoFullScreen = onVideoFullScreen,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_minimal))
                )
            }
        }
    }
}
