package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.models.PostBodyUi
import com.shalev.hamal.utils.parsePostBodyHtml
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private const val VIDEO_DELAY = 1000L
private const val UNEXPANDED_TEXT_LENGTH = 120

@Composable
fun PostBody(
    postBody: ImmutableList<PostBodyUi>,
    galleryItems: ImmutableList<PostBodyUi.Gallery>,
    isExpanded: Boolean,
    currentlyPlayingMedia: String?,
    exoPlayer: ExoPlayer,
    onPlayMedia: (id: String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    onPictureClick: ((url: String) -> Unit)?,
    modifier: Modifier = Modifier
) {
    var delayPassed by remember { mutableStateOf(false) }

    if (!isExpanded) {
        LaunchedEffect(Unit) {
            delay(VIDEO_DELAY.milliseconds)
            delayPassed = true
        }
    }

    val linkColor = MaterialTheme.colorScheme.secondary
    val (parsedTexts, hasMore) = remember(postBody, linkColor, isExpanded) {
        parsePostBodyHtml(
            postBody,
            linkColor,
            if (isExpanded) Int.MAX_VALUE else UNEXPANDED_TEXT_LENGTH
        )
    }
    val lastParsedId = parsedTexts.keys.lastOrNull()

    Column(modifier = modifier) {
        postBody.forEachIndexed { index, content ->
            key(content.id) {
                when (content) {
                    is PostBodyUi.Title -> {
                        Text(
                            text = content.value.trim(),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    is PostBodyUi.Text -> {
                        val annotatedString = parsedTexts[content.id] ?: return@forEachIndexed
                        val showReadMore = hasMore && lastParsedId == content.id

                        Text(text = if (showReadMore) annotatedString + AnnotatedString("...") else annotatedString)

                        if (showReadMore) {
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
                            Text(
                                text = stringResource(R.string.read_more),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    is PostBodyUi.Picture -> {
                        PostPicture(
                            url = content.value,
                            aspectRatio = content.aspectRatio,
                            onClick = onPictureClick
                        )
                    }

                    is PostBodyUi.Gallery -> {
                        if (isExpanded) {
                            PostPicture(
                                url = content.value,
                                aspectRatio = content.aspectRatio,
                                onClick = onPictureClick
                            )
                        } else {
                            PostGallery(items = galleryItems, onClick = onPictureClick)
                        }
                    }

                    is PostBodyUi.Video -> {
                        PostVideo(
                            id = content.id,
                            value = content.value,
                            aspectRatio = content.aspectRatio,
                            exoPlayer = exoPlayer,
                            isExpanded = isExpanded,
                            shouldPlay = currentlyPlayingMedia == content.id && (isExpanded || delayPassed),
                            isFullScreen = false,
                            onPlayVideo = { id -> onPlayMedia(id) },
                            onVideoFullScreen = onVideoFullScreen,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is PostBodyUi.Podcast -> {
                        PostPodcast(
                            id = content.id,
                            url = content.value,
                            exoPlayer = exoPlayer,
                            shouldPlay = currentlyPlayingMedia == content.id,
                            onPlayClick = { id -> onPlayMedia(id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is PostBodyUi.Embed -> {
                        PostEmbed(
                            content.value,
                            size = content.dimensions,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> {
                        Text((content as PostBodyUi.Unimplemented).value)
                    }
                }

                if (index < postBody.lastIndex) {
                    Spacer(
                        modifier = Modifier.height(
                            if (content is PostBodyUi.Title) dimensionResource(
                                R.dimen.padding_small
                            ) else dimensionResource(R.dimen.padding_medium)
                        )
                    )
                }
            }
        }
    }
}
