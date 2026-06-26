package com.shalev.hamal

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.shalev.hamal.data.AppContainer
import com.shalev.hamal.data.DefaultAppContainer
import com.shalev.hamal.network.AddWidthParamInterceptor
import com.shalev.hamal.utils.Constants
import com.shalev.hamal.utils.WebViewPool
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import java.net.URISyntaxException

class HamalApplication : Application(), SingletonImageLoader.Factory {
    lateinit var mSocket: Socket
        private set
    lateinit var container: AppContainer
        private set

    lateinit var webViewPool: WebViewPool
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        webViewPool = WebViewPool(this)
        webViewPool.preWarm()

        try {
            val opts: IO.Options = IO.Options()
            opts.transports = arrayOf("websocket")
            mSocket = IO.socket(Constants.API_URL, opts)
            mSocket.connect()
        } catch (_: URISyntaxException) {
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(AddWidthParamInterceptor())
            }
            .interceptorCoroutineContext(Dispatchers.IO)
            .build()
    }
}
