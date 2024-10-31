package com.shalev.hamal.utils

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.optionalClickablePicture(url: String, onClick: ((url: String) -> Unit)?): Modifier {
    return if (onClick != null) this.clickable { onClick(url) } else this
}

fun Modifier.optionalClickable(onClick: (() -> Unit)?): Modifier {
    return if (onClick != null) {
        this.clickable { onClick() }
    } else this
}