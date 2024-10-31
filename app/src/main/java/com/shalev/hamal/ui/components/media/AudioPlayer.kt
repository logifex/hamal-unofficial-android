package com.shalev.hamal.ui.components.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shalev.hamal.R
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun AudioPlayer(
    isPlaying: Boolean,
    position: Long,
    duration: Long,
    onPlayClick: () -> Unit,
    onPositionChanged: (position: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val format = stringResource(R.string.duration_format)

    val positionText = remember(position) {
        val positionAsDuration = position.toDuration(DurationUnit.MILLISECONDS)
        val positionMinutes = positionAsDuration.inWholeMinutes
        val positionSeconds = (positionAsDuration - positionMinutes.minutes).inWholeSeconds
        String.format(format, positionMinutes, positionSeconds)
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier
                    .background(Color.White, CircleShape)
                    .size(dimensionResource(R.dimen.play_button_size))
            ) {
                if (isPlaying) {
                    Icon(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = stringResource(R.string.play_audio),
                        tint = Color.Black
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.stop_audio),
                        tint = Color.Black
                    )
                }
            }
            Text(text = positionText)
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        Slider(
            value = position.toFloat(),
            onValueChange = { onPositionChanged(it.toLong()) },
            valueRange = 0f..duration.toFloat(),
            modifier = Modifier
                .align(Alignment.Top)
                .fillMaxHeight()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00FF00)
@Composable
fun PreviewAudioPlayer() {
    AudioPlayer(false, 0, 1, {}, {})
}
