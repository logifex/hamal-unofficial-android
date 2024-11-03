package com.shalev.hamal.ui.components.media

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EmbedWebView(htmlContent: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val webView = remember(htmlContent) {
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
        }
    }

    LaunchedEffect(webView) {
        webView.loadData(
            """
                <html>
                    <head><style>iframe {max-width: 100%;}</style></head>
                    <body>$htmlContent</body>
                </html>
            """, "text/html", "utf-8"
        )
    }

    AndroidView(
        factory = { webView },
        modifier = modifier
    )
}