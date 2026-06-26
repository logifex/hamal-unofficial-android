package com.shalev.hamal.network

import androidx.core.net.toUri
import coil3.intercept.Interceptor
import coil3.request.ImageResult
import coil3.size.Dimension

class AddWidthParamInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val widthPx = (chain.size.width as? Dimension.Pixels)?.px

        if (widthPx != null && request.data is String) {
            val url = (request.data as String).toUri()

            if (url.getQueryParameter("width") != null) {
                return chain.withRequest(request).proceed()
            }

            val newUri = url.buildUpon()
                .appendQueryParameter("width", widthPx.toString())
                .build()

            val newRequest = request.newBuilder().data(newUri).build()

            return chain.withRequest(newRequest).proceed()
        }

        return chain.withRequest(request).proceed()
    }
}