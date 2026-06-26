package com.shalev.hamal.ui.components.post

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.shalev.hamal.models.Dimensions
import com.shalev.hamal.ui.providers.LocalWebViewPool
import com.shalev.hamal.utils.Constants

private const val MARGIN_SIZE = 8

@Composable
fun PostEmbed(htmlContent: String, size: Dimensions, modifier: Modifier = Modifier) {
    val pool = LocalWebViewPool.current
    val webView = remember(pool) { pool.getOrCreate() }
    var isReady by remember { mutableStateOf(false) }

    DisposableEffect(pool, webView) {
        onDispose {
            pool.release(webView)
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val effectiveWidth = maxWidth - MARGIN_SIZE.dp * 2
        val scale =
            if (effectiveWidth < size.width.dp) (effectiveWidth.value / size.width) else 1f
        val frameHeight = size.height * scale
        val totalHeight = frameHeight.dp + (MARGIN_SIZE * 2).dp

        val pageHtml = remember(htmlContent, frameHeight) {
            """
                <html>
                    <head><style>
                        iframe {max-width: 100%; height: ${frameHeight.toInt()}px !important;}
                        body {margin: ${MARGIN_SIZE}px;}                
                    </style></head>
                    <body>$htmlContent</body>
                </html>
            """.trimIndent()
        }

        LaunchedEffect(webView, pageHtml) {
            webView.visibility = View.INVISIBLE
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    webView.visibility = View.VISIBLE
                    isReady = true
                }
            }
            webView.loadDataWithBaseURL(
                Constants.WEBSITE_URL, pageHtml, "text/html", "utf-8", null
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalHeight)
                .clip(MaterialTheme.shapes.small)
        ) {
            AndroidView(
                factory = { webView },
                modifier = Modifier.fillMaxSize()
            )
            if (!isReady) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                )
            }
        }
    }
}
