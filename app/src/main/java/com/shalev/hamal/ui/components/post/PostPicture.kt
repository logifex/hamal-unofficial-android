package com.shalev.hamal.ui.components.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.shalev.hamal.R
import com.shalev.hamal.utils.optionalClickablePicture

@Composable
fun PostPicture(
    url: String,
    onClick: ((utl: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.picture),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth()
            .optionalClickablePicture(url, onClick)
    )
}