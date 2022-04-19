package id.derysudrajat.storyapp.repo.remote.network

import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import id.derysudrajat.storyapp.repo.remote.response.DataStoryResponse
import id.derysudrajat.storyapp.repo.remote.response.LoginResponse
import id.derysudrajat.storyapp.repo.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryService {

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("register")
    suspend fun register(@Body registerBody: RegisterBody): RegisterResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") header: String
    ): DataStoryResponse

    @Multipart
    @POST("stories")
    suspend fun postNewStory(
        @Header("Authorization") header: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): RegisterResponse
}