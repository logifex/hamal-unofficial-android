package com.shalev.hamal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.times
import com.shalev.hamal.R
import com.shalev.hamal.models.Post
import kotlinx.collections.immutable.ImmutableList

const val MAX_NOTIFICATIONS = 12

@Composable
fun PostsNotification(
    posts: ImmutableList<Post>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
            .wrapContentWidth()
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .clickable { onClick() }
            .padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_small)
            )) {
        val pictureSize = dimensionResource(R.dimen.profile_picture_size)
        val overlap = dimensionResource(R.dimen.padding_medium)
        val displayedPosts = posts.take(MAX_NOTIFICATIONS)

        Box(
            modifier = Modifier.width(
                pictureSize + (pictureSize - overlap) * (displayedPosts.size - 1).coerceAtLeast(0)
            )
        ) {
            displayedPosts.forEachIndexed { index, post ->
                ProfilePicture(
                    url = post.writer.avatar,
                    description = post.writer.displayName,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .absoluteOffset(
                            x = index * (pictureSize - overlap)
                        )
                        .border(
                            dimensionResource(R.dimen.border_medium),
                            MaterialTheme.colorScheme.onPrimary,
                            CircleShape
                        ),
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.arrow),
            contentDescription = stringResource(R.string.new_posts),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}