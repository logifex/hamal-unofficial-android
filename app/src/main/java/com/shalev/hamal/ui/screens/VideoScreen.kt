package com.shalev.hamal.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.ui.components.media.VideoPlayer
import com.shalev.hamal.utils.extractVideoResolutionFromUrl
import kotlinx.coroutines.launch

@Composable
fun VideoScreen(
    url: String,
    exoPlayer: ExoPlayer,
    isFocused: Boolean,
    onExitFullScreen: () -> Unit
) {
    val aspectRatio = remember(url) {
        extractVideoResolutionFromUrl(url)
    }

    val mediaSource = remember(url) {
        MediaItem.fromUri(url)
    }

    LaunchedEffect(mediaSource, isFocused, exoPlayer) {
        if (isFocused) {
            launch {
                exoPlayer.run {
                    setMediaItem(mediaSource)
                    prepare()
                    volume = 1f
                    playWhenReady = true
                }
            }
        } else {
            exoPlayer.run {
                stop()
                clearMediaItems()
            }
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (isFocused) {
            VideoPlayer(
                url = url,
                exoPlayer = exoPlayer,
                isFullScreen = true,
                onVideoFullScreen = { onExitFullScreen() },
                modifier = Modifier.aspectRatio(aspectRatio)
            )
        }
    }
}