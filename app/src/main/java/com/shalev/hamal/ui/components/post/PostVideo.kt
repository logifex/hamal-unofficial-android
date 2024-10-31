package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.shalev.hamal.R
import com.shalev.hamal.models.VideoValue
import com.shalev.hamal.ui.components.media.VideoPlayer
import com.shalev.hamal.utils.extractVideoResolutionFromUrl
import kotlinx.coroutines.launch

@Composable
fun PostVideo(
    id: String,
    value: VideoValue,
    exoPlayer: ExoPlayer,
    isExpanded: Boolean,
    shouldPlay: Boolean,
    onPlayVideo: (id: String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    isFullScreen: Boolean,
    modifier: Modifier = Modifier
) {
    val aspectRatio = remember(value.url) {
        extractVideoResolutionFromUrl(value.url)
    }

    val mediaSource = remember(value) {
        MediaItem.fromUri(value.url)
    }

    LaunchedEffect(mediaSource, shouldPlay, exoPlayer, isExpanded) {
        if (shouldPlay) {
            launch {
                exoPlayer.run {
                    setMediaItem(mediaSource)
                    prepare()
                    playWhenReady = true
                    volume = if (isExpanded) 1f else 0f
                }
            }
        }
    }

    Box(modifier = modifier
        .aspectRatio(aspectRatio)
        .background(Color.Black)
        .clickable { onPlayVideo(id) }) {
        if (shouldPlay) {
            VideoPlayer(
                url = value.url,
                exoPlayer = exoPlayer,
                isFullScreen = isFullScreen,
                onVideoFullScreen = onVideoFullScreen,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            value.previewUrl?.let {
                AsyncImage(
                    model = value.previewUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .border(
                        BorderStroke(dimensionResource(R.dimen.border_large), Color.White),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.play_video),
                    tint = Color.White,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.play_button_size))
                        .padding(dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}