package com.example.githubuser.repo.network.service

import com.example.githubuser.repo.network.response.SearchGithubResponse
import com.example.githubuser.repo.network.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    @Headers("Authorization: token f8e76694565401a27736d73867799daa2a8be4ff")
    suspend fun searchUserByUsername(
        @Query("q") username: String
    ): SearchGithubResponse

    @GET("users/{username}")
    @Headers("Authorization: token f8e76694565401a27736d73867799daa2a8be4ff")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): UserResponse
}