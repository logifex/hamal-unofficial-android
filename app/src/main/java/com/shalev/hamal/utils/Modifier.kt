package com.shalev.hamal.utils

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.optionalClickablePicture(url: String, onClick: ((url: String) -> Unit)?): Modifier {
    return onClick?.let { this.clickable { it(url) } } ?: this
}

fun Modifier.optionalClickable(onClick: (() -> Unit)?): Modifier {
    return onClick?.let { this.clickable { it() } } ?: this
}