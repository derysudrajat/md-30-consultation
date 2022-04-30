package id.derysudrajat.storyapp.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.google.android.gms.maps.model.LatLng
import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.data.model.toEntities
import id.derysudrajat.storyapp.data.repository.StoryRepositoryImpl
import id.derysudrajat.storyapp.repo.local.LocalDataSource
import id.derysudrajat.storyapp.repo.remote.RemoteDataSource
import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : StoryRepositoryImpl {
    override suspend fun login(loginBody: LoginBody): Flow<States<LoginResult>> =
        remoteDataSource.login(loginBody)

    override suspend fun register(registerBody: RegisterBody): Flow<States<String>> =
        remoteDataSource.register(registerBody)

    override fun getAllStory(token: String) = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { StoryPagingSource(token, remoteDataSource) }
    ).liveData

    override suspend fun postNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latLng: LatLng
    ): Flow<States<String>> = remoteDataSource.postNewStory(token, file, description, latLng)

    override fun getLocalStories() = localDataSource.getAllStories()
    override suspend fun addAllLocalStories(stories: List<Story>) =
        localDataSource.addAllStories(stories.toEntities())

    override suspend fun deleteAllLocalStories() = localDataSource.deleteAllStories()
}