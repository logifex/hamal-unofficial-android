package com.shalev.hamal.data

import kotlinx.serialization.json.Json

object JsonProvider {
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}