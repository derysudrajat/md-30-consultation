package id.derysudrajat.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.States
import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepositoryImpl {
    suspend fun login(loginBody: LoginBody): Flow<States<LoginResult>>
    suspend fun register(registerBody: RegisterBody): Flow<States<String>>
    fun getAllStory(token: String): LiveData<PagingData<Story>>
    suspend fun postNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latLng: LatLng
    ): Flow<States<String>>

    fun getLocalStories(): Flow<List<Story>>
    suspend fun addAllLocalStories(stories: List<Story>)
    suspend fun deleteAllLocalStories()
}