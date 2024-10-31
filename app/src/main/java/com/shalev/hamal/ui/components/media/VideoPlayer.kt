package com.shalev.hamal.ui.components.media

import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.shalev.hamal.R

@Composable
fun VideoPlayer(
    url: String,
    exoPlayer: ExoPlayer,
    isFullScreen: Boolean,
    onVideoFullScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    player = exoPlayer
                }
            }
        )
        IconButton(
            onClick = { onVideoFullScreen(url) }, modifier = Modifier
                .align(Alignment.TopStart)
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Icon(
                painter = painterResource(if (isFullScreen) R.drawable.fullscreen_exit else R.drawable.fullscreen),
                contentDescription = stringResource(R.string.toggle_full_screen),
                tint = Color.White
            )
        }
    }
}