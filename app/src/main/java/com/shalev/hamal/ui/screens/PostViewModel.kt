package com.shalev.hamal.ui.screens

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.shalev.hamal.HamalApplication
import com.shalev.hamal.data.PostRepository
import com.shalev.hamal.data.JsonProvider
import com.shalev.hamal.data.PostUiState
import com.shalev.hamal.models.FetchingError
import com.shalev.hamal.models.Post
import com.shalev.hamal.models.flatten
import com.shalev.hamal.models.toPostUiItem
import com.shalev.hamal.utils.Constants
import io.socket.emitter.Emitter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

class PostViewModel(
    private val id: String?,
    private val slug: String?,
    private val postRepository: PostRepository,
    application: HamalApplication
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    private val mSocket = application.mSocket

    private val onItemUpdate = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val post = JsonProvider.json.decodeFromString(Post.serializer(), data.toString())

        _uiState.update { prevState ->
            if (prevState is PostUiState.Success && post.id == prevState.post.id) {
                PostUiState.Success(
                    post.copy(comments = prevState.post.data.comments).toPostUiItem(),
                    prevState.flattenedComments
                )
            } else {
                prevState
            }
        }
    }

    private val onItemDelete = Emitter.Listener { args ->
        val data = args[0] as String
        _uiState.update { prevState ->
            if (prevState is PostUiState.Success && data == prevState.post.id) {
                PostUiState.Success(
                    prevState.post.copy(data = prevState.post.data.copy(active = false)),
                    prevState.flattenedComments
                )
            } else {
                prevState
            }
        }
    }

    init {
        getPost()
        mSocket.on(Constants.SocketEvents.ITEM_UPDATE, onItemUpdate)
        mSocket.on(Constants.SocketEvents.ITEM_DELETE, onItemDelete)
    }

    private fun getPost() {
        viewModelScope.launch {
            _uiState.value = try {
                val post = when {
                    id != null -> postRepository.getPost(id)
                    slug != null -> postRepository.getPostBySlug(slug)
                    else -> null
                }
                if (post != null) {
                    PostUiState.Success(
                        post.toPostUiItem(),
                        post.comments?.flatten()?.toImmutableList()
                    )
                } else {
                    PostUiState.Error(FetchingError.NetworkError)
                }
            } catch (_: IOException) {
                PostUiState.Error(FetchingError.NetworkError)
            } catch (e: HttpException) {
                PostUiState.Error(FetchingError.HttpError(e.code()))
            }
        }
    }

    override fun onCleared() {
        mSocket.off(Constants.SocketEvents.ITEM_UPDATE, onItemUpdate)
        mSocket.off(Constants.SocketEvents.ITEM_DELETE, onItemDelete)
    }

    companion object {
        fun Factory(id: String?, slug: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HamalApplication)
                val postRepository = application.container.postRepository
                PostViewModel(id = id, slug = slug, postRepository = postRepository, application)
            }
        }
    }
}