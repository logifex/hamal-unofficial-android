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
import com.shalev.hamal.models.Post
import com.shalev.hamal.utils.Constants
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject

class PostViewModel(
    private val id: String,
    private val postRepository: PostRepository,
    application: HamalApplication
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    private val mSocket = application.mSocket

    private val onItemUpdate = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val post = JsonProvider.json.decodeFromString(Post.serializer(), data.toString())

        if (post.id == id) {
            _uiState.update { prevState ->
                if (prevState is PostUiState.Success) {
                    PostUiState.Success(post.copy(comments = prevState.post.comments))
                } else {
                    prevState
                }
            }
        }
    }

    private val onItemDelete = Emitter.Listener { args ->
        val data = args[0] as String
        if (data == id) {
            _uiState.update { prevState ->
                if (prevState is PostUiState.Success) {
                    PostUiState.Success(prevState.post.copy(active = false))
                } else {
                    prevState
                }
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
                PostUiState.Success(postRepository.getPost(id))
            } catch (e: IOException) {
                PostUiState.Error
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mSocket.off(Constants.SocketEvents.ITEM_UPDATE, onItemUpdate)
        mSocket.off(Constants.SocketEvents.ITEM_DELETE, onItemDelete)
    }

    companion object {
        fun Factory(id: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HamalApplication)
                val postRepository = application.container.postRepository
                PostViewModel(id = id, postRepository = postRepository, application)
            }
        }
    }
}