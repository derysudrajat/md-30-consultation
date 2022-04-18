package id.derysudrajat.storyapp.repo

import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.data.repository.StoryRepositoryImpl
import id.derysudrajat.storyapp.repo.remote.RemoteDataSource
import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : StoryRepositoryImpl {
    override suspend fun login(loginBody: LoginBody): Flow<States<LoginResult>> =
        remoteDataSource.login(loginBody)

    override suspend fun register(registerBody: RegisterBody): Flow<States<String>> =
        remoteDataSource.register(registerBody)

    override suspend fun getAllStory(token: String): Flow<States<List<Story>>> =
        remoteDataSource.getAllStory(token)
}