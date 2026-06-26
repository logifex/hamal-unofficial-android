package com.shalev.hamal.ui.providers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.shalev.hamal.HamalApplication
import com.shalev.hamal.utils.WebViewPool

val LocalWebViewPool = staticCompositionLocalOf<WebViewPool> {
    error("No WebViewPool provided")
}

@Composable
fun WebViewPoolProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val webViewPool = (context.applicationContext as HamalApplication).webViewPool

    CompositionLocalProvider(LocalWebViewPool provides webViewPool) {
        content()
    }
}
