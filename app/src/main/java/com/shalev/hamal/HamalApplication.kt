package com.shalev.hamal

import android.app.Application
import android.webkit.WebView
import com.shalev.hamal.data.AppContainer
import com.shalev.hamal.data.DefaultAppContainer
import com.shalev.hamal.utils.Constants
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class HamalApplication : Application() {
    lateinit var mSocket: Socket
        private set
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        // Initialize a web view to prevent initial loading lag
        WebView(this)

        try {
            val opts: IO.Options = IO.Options()
            opts.transports = arrayOf("websocket")
            mSocket = IO.socket(Constants.API_URL, opts)
        } catch (_: URISyntaxException) {
        }
    }
}