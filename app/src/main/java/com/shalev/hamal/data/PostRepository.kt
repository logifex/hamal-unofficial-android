package com.shalev.hamal.data

import com.shalev.hamal.models.Post
import com.shalev.hamal.network.HamalApiService

interface PostRepository {
    suspend fun getPosts(from: Long? = null): List<Post>
    suspend fun getPost(id: String): Post
    suspend fun getPostBySlug(slug: String): Post
}

class NetworkPostRepository(private val hamalApiService: HamalApiService) : PostRepository {
    override suspend fun getPosts(from: Long?): List<Post> {
        return hamalApiService.getItems(from).data
    }

    override suspend fun getPost(id: String): Post {
        return hamalApiService.getItem(id).item
    }

    override suspend fun getPostBySlug(slug: String): Post {
        return hamalApiService.getItemBySlug(slug).item
    }
}