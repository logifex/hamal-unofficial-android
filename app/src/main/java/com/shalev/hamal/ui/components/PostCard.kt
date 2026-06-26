package com.shalev.hamal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shalev.hamal.R
import com.shalev.hamal.utils.getRelativeTimeString
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PostCard(
    avatar: String,
    displayName: String,
    publishedAt: Long,
    modifier: Modifier = Modifier,
    onContentClick: (() -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val time = remember(publishedAt) {
        val instant = Instant.ofEpochMilli(publishedAt)
        val formatter =
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault())
        formatter.format(instant)
    }
    val context = LocalContext.current

    Card(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_small),
                vertical = dimensionResource(R.dimen.padding_medium)
            )
        ) {
            ProfilePicture(
                url = avatar,
                modifier = Modifier.clickable(
                    enabled = onContentClick != null,
                    onClick = { onContentClick?.invoke() }
                )
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = onContentClick != null,
                            onClick = { onContentClick?.invoke() }),
                ) {
                    Text(
                        text = stringResource(
                            R.string.post_time,
                            time,
                            getRelativeTimeString(publishedAt, context)
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = displayName,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Box(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))) {
                        content()
                    }
                }
                footerContent?.let {
                    it()
                }
            }
        }
    }
}

@Preview
@Composable
fun PostCardPreview() {
    PostCard(
        "https://image-resizer.walla.cloud/image/1685264691981_image_512x512.png?width=100",
        "מערכת חמ\"ל",
        System.currentTimeMillis()
    ) {}
}