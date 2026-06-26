package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.media3.exoplayer.ExoPlayer
import com.shalev.hamal.R
import com.shalev.hamal.models.Post
import com.shalev.hamal.ui.components.PostCard
import com.shalev.hamal.utils.Constants
import androidx.core.graphics.toColorInt
import com.shalev.hamal.models.PostBodyUi
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PostLayout(
    post: Post,
    displayedBody: ImmutableList<PostBodyUi>,
    galleryItems: ImmutableList<PostBodyUi.Gallery>,
    isExpanded: Boolean,
    currentlyPlayingMedia: String?,
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    onPostClick: ((id: String) -> Unit)? = null,
    onPictureClick: ((url: String) -> Unit)? = null,
    onPlayMedia: (id: String) -> Unit,
    onVideoFullScreen: (String) -> Unit,
) {
    val onContentClick = onPostClick?.let { { onPostClick(post.id) } }

    PostCard(
        avatar = post.writer.avatar,
        displayName = post.writer.displayName,
        publishedAt = post.publishedAt,
        onContentClick = onContentClick,
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
                if (post.stampData.isActive) {
                    Text(
                        text = post.stampData.text,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(post.stampData.color.toColorInt()))
                            .padding(
                                horizontal = dimensionResource(R.dimen.padding_small),
                                vertical = dimensionResource(R.dimen.padding_minimal)
                            )
                    )
                }
                PostBody(
                    postBody = displayedBody,
                    galleryItems = galleryItems,
                    isExpanded = isExpanded,
                    currentlyPlayingMedia = currentlyPlayingMedia,
                    exoPlayer = exoPlayer,
                    onPictureClick = onPictureClick,
                    onPlayMedia = onPlayMedia,
                    onVideoFullScreen = onVideoFullScreen
                )
            }
        },
        footerContent = {
            if (post.hashtags.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
                ) {
                    post.hashtags.forEach { hashtag ->
                        key(hashtag.id) {
                            Text(
                                text = "# ${hashtag.text}",
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
            }
            PostFooter(
                likesCount = post.likes.count,
                commentsCount = post.commentsCount,
                shareUrl = "${Constants.WEBSITE_URL}/${post.metaData.slug}",
                onCommentsClick = onContentClick
            )
        },
        modifier = modifier
    )
}