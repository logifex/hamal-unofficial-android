package com.shalev.hamal.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import coil3.compose.AsyncImage
import com.shalev.hamal.R

const val PROFILE_SIZE_PX = 100

@Composable
fun ProfilePicture(
    url: String,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    AsyncImage(
        model = "$url?width=$PROFILE_SIZE_PX",
        contentDescription = description,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .size(dimensionResource(R.dimen.profile_picture_size))
    )
}