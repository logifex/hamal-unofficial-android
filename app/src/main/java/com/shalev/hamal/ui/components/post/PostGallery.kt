package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.shalev.hamal.R
import com.shalev.hamal.models.PostBodyUi
import kotlinx.collections.immutable.ImmutableList

private const val ITEMS_IN_ROW = 3

@Composable
fun PostGallery(
    items: ImmutableList<PostBodyUi.Gallery>,
    modifier: Modifier = Modifier,
    onClick: ((url: String) -> Unit)?
) {
    val firstItem = items.firstOrNull() ?: return

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
        AsyncImage(
            model = firstItem.value,
            contentDescription = stringResource(R.string.picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.small)
                .clickable(
                    enabled = onClick != null,
                    onClick = { onClick?.invoke(firstItem.value) })
        )
        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
            for (i in 1..ITEMS_IN_ROW) {
                val item = items.getOrNull(i)
                if (item != null) {
                    key(item.id) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(MaterialTheme.shapes.small)
                                .clickable(
                                    enabled = onClick != null,
                                    onClick = { onClick?.invoke(item.value) })
                        ) {
                            AsyncImage(
                                model = item.value,
                                contentDescription = stringResource(R.string.picture),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            if (i == ITEMS_IN_ROW && items.size > ITEMS_IN_ROW + 1) {
                                Text(
                                    text = "+${items.size - (ITEMS_IN_ROW + 1)}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.displaySmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .wrapContentSize(
                                            Alignment.Center
                                        )
                                )
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}