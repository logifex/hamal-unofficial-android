package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.shalev.hamal.R

@Composable
fun PostPicture(
    url: String,
    modifier: Modifier = Modifier,
    onClick: ((utl: String) -> Unit)? = null
) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.picture),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke(url) })
    )
}