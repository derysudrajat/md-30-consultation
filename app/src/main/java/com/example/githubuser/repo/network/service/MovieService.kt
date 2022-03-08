package com.example.githubuser.repo.network.service

import com.example.githubuser.repo.network.response.ResponseListMovie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = "0a597bad68c0b95d5fab612cff9d8891",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int? = 1,
    ): ResponseListMovie

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = "0a597bad68c0b95d5fab612cff9d8891",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int? = 1,
    ): ResponseListMovie


}