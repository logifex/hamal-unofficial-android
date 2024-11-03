package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.models.PostBody
import com.shalev.hamal.ui.components.media.EmbedWebView
import com.shalev.hamal.ui.components.LinkText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val VIDEO_DELAY = 1000L

@Composable
fun PostBody(
    postBody: List<PostBody>,
    isExpanded: Boolean,
    currentlyPlayingMedia: String?,
    exoPlayer: ExoPlayer,
    onPlayMedia: (id: String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    onPictureClick: ((url: String) -> Unit)?,
    modifier: Modifier = Modifier
) {
    var delayPassed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isExpanded) {
            launch {
                delay(VIDEO_DELAY)
                delayPassed = true
            }
        }
    }

    Column(modifier = modifier) {
        Text(
            text = (postBody[0] as PostBody.Title).value.trim(),
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))) {
            var characters = 0
            postBody.drop(1).forEach { content ->
                when (content) {
                    is PostBody.Title -> {}

                    is PostBody.Text -> {
                        val trimmedValue = content.value.trim()

                        if (isExpanded) {
                            LinkText(
                                text = trimmedValue
                            )
                        } else if (characters < 120) {
                            val text =
                                if (characters + content.value.length <= 120) trimmedValue
                                else content.value.substring(0, 120 - characters).trim() + "..."

                            LinkText(
                                text = text,
                            )

                            characters += content.value.length
                            if (characters >= 120) {
                                Text(
                                    text = stringResource(R.string.read_more),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }

                    is PostBody.Picture -> {
                        PostPicture(content.value, onPictureClick)
                    }

                    is PostBody.Gallery -> {
                        if (isExpanded) {
                            PostPicture(content.value, onPictureClick)
                        } else {
                            val items = postBody.filterIsInstance<PostBody.Gallery>()

                            PostGallery(items, onPictureClick)
                        }
                    }

                    is PostBody.Video -> {
                        PostVideo(
                            id = content.id,
                            value = content.value,
                            exoPlayer = exoPlayer,
                            isExpanded = isExpanded,
                            shouldPlay = currentlyPlayingMedia == content.id && (isExpanded || delayPassed),
                            isFullScreen = false,
                            onPlayVideo = { onPlayMedia(it) },
                            onVideoFullScreen = onVideoFullScreen,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is PostBody.Podcast -> {
                        PostPodcast(
                            id = content.id,
                            url = content.value,
                            exoPlayer = exoPlayer,
                            shouldPlay = currentlyPlayingMedia == content.id,
                            onPlayClick = { onPlayMedia(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is PostBody.Embed -> {
                        EmbedWebView(content.value, modifier = Modifier.fillMaxWidth())
                    }

                    else -> {
                        Text(content.value.toString())
                    }
                }

                if (!isExpanded && content !is PostBody.Title && content !is PostBody.Text) {
                    return
                }
            }
        }
    }
}