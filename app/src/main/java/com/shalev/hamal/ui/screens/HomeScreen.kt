package com.shalev.hamal.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shalev.hamal.R
import com.shalev.hamal.data.HomeUiState
import com.shalev.hamal.models.FetchingError
import com.shalev.hamal.ui.AppBar
import com.shalev.hamal.ui.components.Posts
import com.shalev.hamal.ui.components.PostsNotification
import com.shalev.hamal.ui.components.LoadingIndicator
import com.shalev.hamal.ui.components.Message
import com.shalev.hamal.ui.providers.LocalExoPlayer
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    isFocused: Boolean,
    onPostClick: (String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    homeViewModel: HomePostsViewModel = viewModel(factory = HomePostsViewModel.Factory)
) {
    val homeUiState = homeViewModel.uiState.collectAsState()
    val isRefreshing = homeViewModel.refreshing

    val coroutineScope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    val exoPlayer = LocalExoPlayer.current

    LaunchedEffect(listState, homeUiState.value) {
        if (homeUiState.value is HomeUiState.Loading) {
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(isFocused, exoPlayer) {
        if (!isFocused) {
            exoPlayer.run {
                if (mediaItemCount > 0) {
                    stop()
                    clearMediaItems()
                }
            }
        }
    }

    Scaffold(topBar = {
        AppBar(
            title = stringResource(R.string.app_name),
            onTitleClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (homeUiState.value is HomeUiState.Success) {
            val newPosts = (homeUiState.value as HomeUiState.Success).newPosts

            if (newPosts.isNotEmpty()) {
                LaunchedEffect(listState) {
                    snapshotFlow { listState.firstVisibleItemIndex == 0 }
                        .collect { isTop ->
                            if (isTop) {
                                homeViewModel.resetNewPosts()
                            }
                        }
                }

                LaunchedEffect(listState, newPosts) {
                    if (listState.firstVisibleItemIndex <= 1 && listState.firstVisibleItemScrollOffset <= 0) {
                        listState.scrollToItem(0)
                    }
                }

                PostsNotification(
                    posts = newPosts,
                    onClick = {
                        homeViewModel.resetNewPosts()
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                        .padding(innerPadding)
                        .padding(top = dimensionResource(R.dimen.padding_medium))
                )
            }
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing.value,
            onRefresh = {
                if (homeUiState.value !is HomeUiState.Loading) {
                    isRefreshing.value = true
                    homeViewModel.getPosts()
                }
            },
            state = state,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = innerPadding.calculateTopPadding()),
                    isRefreshing = isRefreshing.value,
                    state = state
                )
            },
        ) {
            when (homeUiState.value) {
                is HomeUiState.Loading -> LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )

                is HomeUiState.Error -> {
                    val state = homeUiState.value as HomeUiState.Error
                    Message(
                        text = when (state.error) {
                            is FetchingError.NetworkError -> stringResource(R.string.network_error)
                            is FetchingError.HttpError -> stringResource(
                                R.string.http_error,
                                state.error.code
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                    )
                }

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
                        modifier = Modifier.padding(
                            top = dimensionResource(R.dimen.padding_small),
                            start = dimensionResource(R.dimen.padding_minimal),
                            end = dimensionResource(R.dimen.padding_minimal)
                        ),
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}
