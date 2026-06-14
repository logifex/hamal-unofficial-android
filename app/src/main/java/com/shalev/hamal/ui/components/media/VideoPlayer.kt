package com.shalev.hamal.ui.components.media

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    url: String,
    exoPlayer: ExoPlayer,
    isFullScreen: Boolean,
    onVideoFullScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val videoProgressColor = MaterialTheme.colorScheme.primary
    val remainingBarColor = MaterialTheme.colorScheme.primaryContainer

    Box(modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    player = exoPlayer
                    setFullscreenButtonState(isFullScreen)
                    setFullscreenButtonClickListener { onVideoFullScreen(url) }

                    val timeBar = findViewById<DefaultTimeBar>(androidx.media3.ui.R.id.exo_progress)
                    timeBar?.let { bar ->
                        bar.setPlayedColor(videoProgressColor.toArgb())
                        bar.setScrubberColor(videoProgressColor.toArgb())
                        bar.setBufferedColor(remainingBarColor.toArgb())
                    }
                }
            })
    }
}