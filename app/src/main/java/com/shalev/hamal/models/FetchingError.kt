package com.shalev.hamal.models

sealed class FetchingError {
    data object NetworkError : FetchingError()
    data class HttpError(val code: Int) : FetchingError()
}
