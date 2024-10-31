package com.shalev.hamal.ui.components.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.shalev.hamal.R
import com.shalev.hamal.models.Comment

@Composable
fun CommentThread(comment: Comment, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CommentLayout(comment = comment)
        if (!comment.replies.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
            ) {
                comment.replies.forEach { reply ->
                    HorizontalDivider(
                        thickness = dimensionResource(R.dimen.border_thin),
                        color = MaterialTheme.colorScheme.inverseSurface
                    )
                    CommentLayout(comment = reply)
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        HorizontalDivider(
            thickness = dimensionResource(R.dimen.border_medium),
            color = MaterialTheme.colorScheme.inverseSurface,
        )
    }
}