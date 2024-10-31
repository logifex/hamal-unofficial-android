package com.shalev.hamal.ui.components.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.shalev.hamal.R
import com.shalev.hamal.models.Comment
import com.shalev.hamal.ui.components.FooterTextIcon
import com.shalev.hamal.ui.components.PostCard

@Composable
fun CommentLayout(comment: Comment, modifier: Modifier = Modifier) {
    PostCard(
        comment.user.avatar,
        comment.user.displayName,
        comment.createdAt,
        modifier = modifier,
        content = {
            Text(text = comment.text.trim())
        },
        footerContent = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                FooterTextIcon(
                    text = if (comment.likes.count > 0) comment.likes.count.toString() else null,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.heart),
                        contentDescription = stringResource(R.string.likes),
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                FooterTextIcon(
                    text = (comment.replies?.size ?: 0).toString(),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.message),
                        contentDescription = stringResource(R.string.replies),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}