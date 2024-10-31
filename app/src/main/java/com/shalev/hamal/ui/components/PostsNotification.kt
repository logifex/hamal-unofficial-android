package com.shalev.hamal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun PostsNotification(posts: List<Post>, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_small)
            )
            .clickable { onClick() }) {
        Box {
            posts.take(12).forEachIndexed { index, post ->
                Row {
                    Spacer(
                        modifier = Modifier.width(
                            index * (dimensionResource(R.dimen.profile_picture_size) - dimensionResource(
                                R.dimen.padding_medium
                            ))
                        )
                    )
                    ProfilePicture(
                        url = post.writer.avatar,
                        description = post.writer.displayName,
                        modifier = Modifier.border(
                                dimensionResource(R.dimen.border_medium),
                                MaterialTheme.colorScheme.onPrimary,
                                CircleShape
                            )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        Icon(
            painter = painterResource(R.drawable.arrow),
            contentDescription = stringResource(R.string.new_posts),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}