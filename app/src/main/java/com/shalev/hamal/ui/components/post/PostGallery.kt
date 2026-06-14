package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.shalev.hamal.R
import com.shalev.hamal.models.PostBody

private const val ITEMS_IN_ROW = 3

@Composable
fun PostGallery(
    items: List<PostBody.Gallery>,
    modifier: Modifier = Modifier,
    onClick: ((url: String) -> Unit)?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
        AsyncImage(
            model = items[0].value,
            contentDescription = stringResource(R.string.picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(
                    enabled = onClick != null,
                    onClick = { onClick?.invoke(items[0].value) }
                )
        )
        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
            for (i in 1..ITEMS_IN_ROW) {
                if (items.size > i) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable(
                                enabled = onClick != null,
                                onClick = { onClick?.invoke(items[i].value) })
                    ) {
                        AsyncImage(
                            model = items[i].value,
                            contentDescription = stringResource(R.string.picture),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (i == ITEMS_IN_ROW && items.size > ITEMS_IN_ROW + 1) {
                            Box(
                                contentAlignment = Alignment.Center, modifier = Modifier
                                    .matchParentSize()
                                    .graphicsLayer { alpha = 0.7f }
                                    .background(Color.Black)) {
                                Text(
                                    text = "+${items.size - (ITEMS_IN_ROW + 1)}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.displaySmall,
                                    textAlign = TextAlign.Center,
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