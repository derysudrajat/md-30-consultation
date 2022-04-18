package id.derysudrajat.storyapp.data.repository

import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.States
import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import kotlinx.coroutines.flow.Flow

interface StoryRepositoryImpl {
    suspend fun login(loginBody: LoginBody): Flow<States<LoginResult>>
    suspend fun register(registerBody: RegisterBody): Flow<States<String>>
    suspend fun getAllStory(token: String): Flow<States<List<Story>>>
}