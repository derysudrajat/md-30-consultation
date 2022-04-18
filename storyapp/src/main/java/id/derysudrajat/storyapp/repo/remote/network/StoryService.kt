package id.derysudrajat.storyapp.repo.remote.network

import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import id.derysudrajat.storyapp.repo.remote.response.DataStoryResponse
import id.derysudrajat.storyapp.repo.remote.response.LoginResponse
import id.derysudrajat.storyapp.repo.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface StoryService {

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("register")
    suspend fun register(registerBody: RegisterBody): RegisterResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") header: String
    ): DataStoryResponse
}