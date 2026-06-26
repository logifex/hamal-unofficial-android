package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.shalev.hamal.R

@Composable
fun PostPicture(
    url: String,
    aspectRatio: Float,
    modifier: Modifier = Modifier,
    onClick: ((utl: String) -> Unit)? = null
) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.picture),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clip(MaterialTheme.shapes.small)
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke(url) })
    )
}