package com.shalev.hamal.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.shalev.hamal.HamalApplication
import com.shalev.hamal.data.HomeUiState
import com.shalev.hamal.data.PostRepository
import com.shalev.hamal.data.JsonProvider
import com.shalev.hamal.models.FetchingError
import com.shalev.hamal.models.Post
import com.shalev.hamal.models.toPostFeedItem
import com.shalev.hamal.utils.Constants
import io.socket.emitter.Emitter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class HomePostsViewModel(
    private val postRepository: PostRepository,
    application: HamalApplication
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var fetchingMore = mutableStateOf(false)
    var refreshing = mutableStateOf(false)

    private val mSocket = application.mSocket

    private val onItemCreate = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val post = JsonProvider.json.decodeFromString(Post.serializer(), data.toString())
        _uiState.update { prevState ->
            if (prevState is HomeUiState.Success) {
                HomeUiState.Success(
                    listOf(post.toPostFeedItem()).plus(prevState.posts).toImmutableList(),
                    prevState.newPosts.plus(post).toImmutableList()
                )
            } else {
                prevState
            }
        }
    }

    private val onItemUpdate = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val post = JsonProvider.json.decodeFromString(Post.serializer(), data.toString())
        _uiState.update { prevState ->
            if (prevState is HomeUiState.Success) {
                val updatedPosts =
                    prevState.posts.map { if (it.id == post.id) post.toPostFeedItem() else it }
                HomeUiState.Success(updatedPosts.toImmutableList(), prevState.newPosts)
            } else {
                prevState
            }
        }
    }

    private val onItemDelete = Emitter.Listener { args ->
        val data = args[0] as String
        _uiState.update { prevState ->
            if (prevState is HomeUiState.Success) {
                HomeUiState.Success(
                    prevState.posts.filter { it.id != data }.toImmutableList(),
                    prevState.newPosts.filter { it.id != data }.toImmutableList()
                )
            } else {
                prevState
            }
        }
    }

    init {
        getPosts()
        mSocket.on(Constants.SocketEvents.ITEM_CREATE, onItemCreate)
        mSocket.on(Constants.SocketEvents.ITEM_UPDATE, onItemUpdate)
        mSocket.on(Constants.SocketEvents.ITEM_DELETE, onItemDelete)
    }

    fun getPosts() {
        _uiState.value = HomeUiState.Loading

        viewModelScope.launch {
            _uiState.value =
                try {
                    HomeUiState.Success(
                        postRepository.getPosts().map { it.toPostFeedItem() }.toImmutableList(),
                        persistentListOf()
                    )
                } catch (_: IOException) {
                    HomeUiState.Error(FetchingError.NetworkError)
                } catch (e: HttpException) {
                    HomeUiState.Error(FetchingError.HttpError(e.code()))
                }
            refreshing.value = false
        }
    }

    fun getMorePosts() {
        if (fetchingMore.value) {
            return
        }
        fetchingMore.value = true

        viewModelScope.launch {
            _uiState.update { prevState ->
                if (prevState is HomeUiState.Success) {
                    try {
                        val newPosts =
                            postRepository.getPosts(prevState.posts.last().data.publishedAt)
                                .map { it.toPostFeedItem() }
                        HomeUiState.Success(
                            prevState.posts.plus(newPosts)
                                .toImmutableList(),
                            prevState.newPosts
                        )
                    } catch (_: IOException) {
                        HomeUiState.Error(FetchingError.NetworkError)
                    } catch (e: HttpException) {
                        HomeUiState.Error(FetchingError.HttpError(e.code()))
                    }
                } else {
                    prevState
                }
            }
            fetchingMore.value = false
        }
    }

    fun resetNewPosts() {
        _uiState.update { prevState ->
            if (prevState is HomeUiState.Success)
                HomeUiState.Success(prevState.posts, persistentListOf())
            else prevState
        }
    }

    override fun onCleared() {
        mSocket.off(Constants.SocketEvents.ITEM_CREATE, onItemCreate)
        mSocket.off(Constants.SocketEvents.ITEM_UPDATE, onItemUpdate)
        mSocket.off(Constants.SocketEvents.ITEM_DELETE, onItemDelete)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HamalApplication)
                val postRepository = application.container.postRepository
                HomePostsViewModel(postRepository = postRepository, application = application)
            }
        }
    }
}