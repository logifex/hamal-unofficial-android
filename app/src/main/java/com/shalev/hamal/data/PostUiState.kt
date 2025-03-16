package com.shalev.hamal.data

import com.shalev.hamal.models.FetchingError
import com.shalev.hamal.models.Post

sealed interface PostUiState {
    data class Success(val post: Post) : PostUiState
    data object Loading : PostUiState
    data class Error(val error: FetchingError) : PostUiState
}
