package com.shalev.hamal.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import com.shalev.hamal.ui.components.media.VideoPlayer
import com.shalev.hamal.ui.providers.LocalExoPlayer

@Composable
fun VideoScreen(
    url: String,
    isFocused: Boolean,
    onExitFullScreen: () -> Unit
) {
    val mediaSource = remember(url) {
        MediaItem.fromUri(url)
    }

    val exoPlayer = LocalExoPlayer.current
    val window = LocalActivity.current?.window
    if (window != null) {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        DisposableEffect(Unit) {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            onDispose {
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    LaunchedEffect(mediaSource, isFocused, exoPlayer) {
        if (isFocused) {
            exoPlayer.run {
                setMediaItem(mediaSource)
                prepare()
                volume = 1f
                playWhenReady = true
            }
        } else {
            exoPlayer.run {
                clearMediaItems()
                stop()
            }
        }
    }

    if (isFocused) {
        VideoPlayer(
            url = url,
            exoPlayer = exoPlayer,
            isFullScreen = true,
            onVideoFullScreen = { onExitFullScreen() },
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        )
    }
}