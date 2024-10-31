package com.shalev.hamal.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.shalev.hamal.R

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier.width(dimensionResource(R.dimen.loading_indicator_size)),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
