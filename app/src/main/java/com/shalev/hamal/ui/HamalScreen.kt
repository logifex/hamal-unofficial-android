package com.shalev.hamal.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shalev.hamal.HamalApplication

enum class Screen {
    Start,
    Post,
    Picture,
    Video
}

const val POST_ID = "postId"
const val POST_SLUG = "postSlug"
const val PICTURE_URL = "pictureUrl"
const val VIDEO_URL = "videoUrl"

@Composable
fun HamalApp(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val application = context.applicationContext as HamalApplication

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var focusedScreen by remember { mutableStateOf(Screen.Start) }

    LaunchedEffect(Unit) {
        application.mSocket.connect()
    }

    ManageExoPlayerLifecycle(exoPlayer)

    Surface {
        Navigation(
            navController = navController,
            exoPlayer = exoPlayer,
            onScreenChange = { focusedScreen = it }
        )
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