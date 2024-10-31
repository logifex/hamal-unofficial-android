package com.shalev.hamal.data

import com.shalev.hamal.models.Post

sealed interface HomeUiState {
    data class Success(
        val posts: List<Post> = emptyList(),
        val newPosts: List<Post> = emptyList()
    ) : HomeUiState

    data object Loading : HomeUiState
    data object Error : HomeUiState
}
