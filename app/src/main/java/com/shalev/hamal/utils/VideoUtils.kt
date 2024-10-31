package com.shalev.hamal.utils

private val regex = Regex("""_(\d+)x(\d+)""")

fun extractVideoResolutionFromUrl(url: String): Float {
    val matchResult = regex.find(url)
    return (matchResult?.groups?.get(1)?.value?.toFloat() ?: 1920f) /
            (matchResult?.groups?.get(2)?.value?.toFloat() ?: 1080f)
}