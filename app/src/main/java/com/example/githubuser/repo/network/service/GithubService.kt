package com.example.githubuser.repo.network.service

import com.example.githubuser.repo.network.response.SearchGithubResponse
import com.example.githubuser.repo.network.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    suspend fun searchUserByUsername(
        @Query("q") username: String
    ): SearchGithubResponse

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): UserResponse

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<UserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<UserResponse>
}
