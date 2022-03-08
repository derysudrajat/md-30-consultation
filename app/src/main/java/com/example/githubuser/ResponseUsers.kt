package com.example.githubuser

data class ResponseUsers(
    val page: Int,
    val perPage: Int,
    val total: Int,
    val totalPage: Int,
    val data: List<MUser>,
    val support: Support
)

data class MUser(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
)

data class Support(
    val url: String,
    val text: String
)
