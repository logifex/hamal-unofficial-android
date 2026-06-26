package com.shalev.hamal.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView

class WebViewPool(private val context: Context) {
    private val pool = ArrayDeque<WebView>()

    fun getOrCreate(): WebView = if (pool.isEmpty()) {
        createWebview()
    } else {
        pool.removeFirst()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun createWebview(): WebView = WebView(context).apply {
        settings.javaScriptEnabled = true
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }


    fun release(webView: WebView) {
        webView.stopLoading()
        webView.loadUrl("about:blank")
        webView.clearHistory()
        webView.visibility = View.VISIBLE
        (webView.parent as? ViewGroup)?.removeView(webView)

        if (pool.size < 2) {
            pool.addLast(webView)
        } else {
            webView.destroy()
        }
    }

    fun preWarm(count: Int = 2) {
        repeat(count - pool.size) { pool.addLast(createWebview()) }
    }
}