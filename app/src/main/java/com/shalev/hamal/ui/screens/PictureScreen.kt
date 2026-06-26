package com.shalev.hamal.ui.screens

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import coil3.compose.AsyncImage
import com.shalev.hamal.R
import com.shalev.hamal.ui.AppBar
import com.shalev.hamal.utils.extractAspectRatioFromUrl

@Composable
fun PictureScreen(url: String, onBackClick: () -> Unit) {
    val aspectRatio = remember(url) { extractAspectRatioFromUrl(url) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    Scaffold(topBar = {
        AppBar(
            title = stringResource(R.string.post_title),
            navigateUp = onBackClick,
        )
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(0.9f, 3f)
                        val newOffset = if (scale <= 1) Offset.Zero else {
                            val maxPanX = (imageSize.width * (scale - 1f)) / 2f
                            val maxPanY = (imageSize.height * (scale - 1f)) / 2f
                            val newOffset = offset + pan

                            Offset(
                                newOffset.x.coerceIn(-maxPanX, maxPanX),
                                newOffset.y.coerceIn(-maxPanY, maxPanY)
                            )
                        }

                        scale = newScale
                        offset = newOffset
                    }
                }
        ) {
            AsyncImage(
                model = url,
                contentDescription = stringResource(R.string.picture),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .onSizeChanged { imageSize = it }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
            )
        }
    }
}