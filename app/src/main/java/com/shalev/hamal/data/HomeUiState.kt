package com.shalev.hamal.data

import com.shalev.hamal.models.FetchingError
import com.shalev.hamal.models.Post
import com.shalev.hamal.models.PostUi
import kotlinx.collections.immutable.ImmutableList

sealed interface HomeUiState {
    data class Success(
        val posts: ImmutableList<PostUi>,
        val newPosts: ImmutableList<Post>
    ) : HomeUiState

    data object Loading : HomeUiState
    data class Error(val error: FetchingError) : HomeUiState
}
