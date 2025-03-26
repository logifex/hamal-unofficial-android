package com.shalev.hamal.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shalev.hamal.HamalApplication
import com.shalev.hamal.R
import kotlinx.coroutines.launch

enum class Screen(@StringRes val title: Int) {
    Start(R.string.app_name),
    Post(R.string.post_title),
    Picture(R.string.post_title),
    Video(R.string.video_screen_title)
}

const val POST_ID = "postId"
const val POST_SLUG = "postSlug"
const val PICTURE_URL = "pictureUrl"
const val VIDEO_URL = "videoUrl"

@Composable
fun HamalApp(
    navController: NavHostController = rememberNavController()
) {
    val mainScreenListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val application = context.applicationContext as HamalApplication

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var focusedScreen by remember { mutableStateOf(Screen.Start) }

    LaunchedEffect(Unit) {
        application.mSocket.connect()
    }

    ManageExoPlayerLifecycle(exoPlayer)

    Scaffold(topBar = {
        AppBar(
            canNavigateBack = navController.previousBackStackEntry != null,
            title = stringResource(focusedScreen.title),
            onTitleClick = {
                if (focusedScreen == Screen.Start) {
                    coroutineScope.launch {
                        mainScreenListState.animateScrollToItem(0)
                    }
                }
            },
            navigateUp = { navController.popBackStack() },
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Navigation(
                navController = navController,
                exoPlayer = exoPlayer,
                mainScreenListState = mainScreenListState,
                onScreenChange = { focusedScreen = it }
            )
        }
    }
}

@Composable
fun ManageExoPlayerLifecycle(exoPlayer: ExoPlayer) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner, exoPlayer) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.run {
                        playWhenReady = false
                        stop()
                    }
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.run {
                        playWhenReady = true
                        if (mediaItemCount > 0) {
                            prepare()
                        }
                    }
                }

                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.run {
                        stop()
                        release()
                    }
                }

                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}