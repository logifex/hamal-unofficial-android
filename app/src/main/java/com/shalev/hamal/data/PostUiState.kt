package com.shalev.hamal.data

import com.shalev.hamal.models.Comment
import com.shalev.hamal.models.FetchingError
import com.shalev.hamal.models.PostUi
import kotlinx.collections.immutable.ImmutableList

sealed interface PostUiState {
    data class Success(
        val post: PostUi,
        val flattenedComments: ImmutableList<Comment>?
    ) : PostUiState

    data object Loading : PostUiState
    data class Error(val error: FetchingError) : PostUiState
}
