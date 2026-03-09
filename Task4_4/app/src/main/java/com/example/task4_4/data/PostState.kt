package com.example.task4_4.data

sealed class PostState {
    data object Loading: PostState()
    data class Ready(
        val post: SocialPost,
        val user: User?,
        val comment: List<Comment>?
    ): PostState()
    data class Error(
        val post: SocialPost
    ): PostState()
}