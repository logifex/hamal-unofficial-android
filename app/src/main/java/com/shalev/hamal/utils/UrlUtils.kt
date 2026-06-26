package com.shalev.hamal.utils

import com.shalev.hamal.models.Dimensions

private const val DEFAULT_ASPECT_RATIO = 1920f / 1080f
private val DEFAULT_EMBED_SIZE = Dimensions(560, 315)

private val RESOLUTION_REGEX = Regex("""_(\d+)x(\d+)""")

private val WIDTH_REGEX = """(?<=[\s"'])width\s*[:=]\s*["']?(\d+)""".toRegex()
private val HEIGHT_REGEX = """(?<=[\s"'])height\s*[:=]\s*["']?(\d+)""".toRegex()

fun extractAspectRatioFromUrl(url: String): Float {
    val matchResult = RESOLUTION_REGEX.find(url)

    val width = matchResult?.groupValues?.get(1)?.toFloat()
    val height = matchResult?.groupValues?.get(2)?.toFloat()

    if (width == null || height == null) {
        return DEFAULT_ASPECT_RATIO
    }

    return width / height
}

fun extractSizeFromEmbed(html: String): Dimensions {
    val width = WIDTH_REGEX.find(html)?.groupValues?.get(1)?.toIntOrNull()
    val height = HEIGHT_REGEX.find(html)?.groupValues?.get(1)?.toIntOrNull()

    if (width == null || height == null) {
        return DEFAULT_EMBED_SIZE
    }

    return Dimensions(width, height)
}