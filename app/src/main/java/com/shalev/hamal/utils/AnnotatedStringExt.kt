package com.shalev.hamal.utils

import androidx.compose.ui.text.AnnotatedString

fun AnnotatedString.trim(): AnnotatedString {
    val start = indexOfFirst { !it.isWhitespace() }
    if (start == -1) {
        return AnnotatedString("")
    }

    val end = indexOfLast { !it.isWhitespace() } + 1

    return this.subSequence(start, end)
}