package com.shalev.hamal.ui.components.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.ui.components.media.AudioPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostPodcast(
    id: String,
    url: String,
    shouldPlay: Boolean,
    exoPlayer: ExoPlayer,
    onPlayClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPosition by remember { mutableLongStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(shouldPlay, exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingChanged: Boolean) {
                isPlaying = isPlayingChanged
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    duration = exoPlayer.duration
                }
            }
        }

        if (shouldPlay) {
            exoPlayer.addListener(listener)
        }

        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    LaunchedEffect(isPlaying, exoPlayer) {
        if (isPlaying) {
            coroutineScope.launch {
                while (isPlaying) {
                    currentPosition = exoPlayer.currentPosition
                    delay(100L)
                }
            }
        }
    }

    val mediaSource = remember(url) {
        MediaItem.fromUri(url)
    }

    LaunchedEffect(mediaSource, shouldPlay, exoPlayer) {
        if (shouldPlay) {
            launch {
                exoPlayer.run {
                    setMediaItem(mediaSource)
                    prepare()
                    volume = 1f
                    playWhenReady = true
                }
            }
        } else {
            isPlaying = false
            currentPosition = 0
        }
    }

    AudioPlayer(
        isPlaying = isPlaying,
        position = currentPosition,
        duration = duration,
        onPlayClick = {
            if (!shouldPlay) {
                onPlayClick(id)
            } else {
                exoPlayer.run {
                    if (!isPlaying) {
                        play()
                    } else {
                        pause()
                    }
                }
            }
        },
        onPositionChanged = { exoPlayer.seekTo(it) },
        modifier = modifier
    )
}