package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.models.Post
import com.shalev.hamal.ui.components.PostCard
import com.shalev.hamal.utils.Constants

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostLayout(
    post: Post,
    isExpanded: Boolean,
    currentlyPlayingMedia: String?,
    exoPlayer: ExoPlayer,
    onPostClick: ((id: String) -> Unit)? = null,
    onPictureClick: ((url: String) -> Unit)? = null,
    onPlayMedia: (id: String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val onContentClick = remember(onPostClick) { onPostClick?.let { { onPostClick(post.id) } } }

    PostCard(
        avatar = post.writer.avatar,
        displayName = post.writer.displayName,
        publishedAt = post.publishedAt,
        onContentClick = onContentClick,
        content = {
            if (post.stampData.text.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(android.graphics.Color.parseColor(post.stampData.color)))
                ) {
                    Text(
                        text = post.stampData.text,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(R.dimen.padding_small),
                            vertical = dimensionResource(R.dimen.padding_minimal)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            }
            PostBody(
                postBody = post.body,
                isExpanded = isExpanded,
                currentlyPlayingMedia = currentlyPlayingMedia,
                exoPlayer = exoPlayer,
                onPictureClick = onPictureClick,
                onPlayMedia = onPlayMedia,
                onVideoFullScreen = onVideoFullScreen
            )
        },
        footerContent = {
            Column {
                if (post.hashtags.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
                    ) {
                        post.hashtags.forEach { hashtag ->
                            Text(
                                text = "# ${hashtag.text}",
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
                PostFooter(
                    likesCount = post.likes.count,
                    commentsCount = post.commentsCount,
                    shareUrl = "${Constants.WEBSITE_URL}/${post.metaData.slug}",
                    onCommentsClick = onContentClick ?: {}
                )
            }
        },
        modifier = modifier
    )
}