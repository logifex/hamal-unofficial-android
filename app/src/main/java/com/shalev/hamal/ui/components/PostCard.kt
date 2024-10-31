package com.shalev.hamal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.shalev.hamal.utils.convertToTimeAgo
import com.shalev.hamal.utils.optionalClickable
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.TimeZone

@Composable
fun PostCard(
    avatar: String,
    displayName: String,
    publishedAt: Long,
    onContentClick: (() -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val instant = remember {
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(publishedAt),
            TimeZone.getDefault().toZoneId()
        )
    }
    val time = instant.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    val context = LocalContext.current

    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_small),
                vertical = dimensionResource(R.dimen.padding_medium)
            )
        ) {
            ProfilePicture(avatar, modifier = Modifier.optionalClickable(onContentClick))
            Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.optionalClickable(onContentClick)) {
                    Column {
                        Text(
                            text = stringResource(
                                R.string.post_time,
                                time,
                                convertToTimeAgo(publishedAt, context)
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = displayName,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))) {
                        content()
                    }
                }
                if (footerContent != null) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                    Column {
                        footerContent()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PostCardPreview() {
    PostCard(
        "https://image-resizer.walla.cloud/image/1685264691981_image_512x512.png",
        "מערכת חמ\"ל",
        System.currentTimeMillis()
    ) {}
}