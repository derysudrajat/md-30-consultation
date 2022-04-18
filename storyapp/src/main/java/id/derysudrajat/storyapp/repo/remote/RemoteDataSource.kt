package id.derysudrajat.storyapp.repo.remote

import android.util.Log
import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.States
import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import id.derysudrajat.storyapp.repo.remote.network.StoryService
import id.derysudrajat.storyapp.repo.remote.response.toLoginResult
import id.derysudrajat.storyapp.repo.remote.response.toStories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val storyService: StoryService) {

    companion object {
        const val TAG = "RemoteDataSource"
    }

    suspend fun login(loginBody: LoginBody) = flow<States<LoginResult>> {
        emit(States.loading())
        val response = storyService.login(loginBody)
        response.let {
            if (it.error != true) emit(States.success(it.loginResultResponse.toLoginResult()))
            else emit(States.failed(it.message ?: ""))
        }

    }.catch {
        Log.d(TAG, "login: failed = ${it.message}")
        emit(States.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    suspend fun register(registerBody: RegisterBody) = flow<States<String>> {
        emit(States.loading())
        val response = storyService.register(registerBody)
        response.let {
            if (it.error != true) emit(States.success(it.message ?: ""))
            else emit(States.failed(it.message ?: ""))
        }
    }.catch {
        Log.d(TAG, "register: failed = ${it.message}")
        emit(States.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllStory(token: String) = flow<States<List<Story>>> {
        emit(States.loading())
        val response = storyService.getAllStory(token)
        response.let {
            if (it.error != true) emit(States.Success(it.story?.toStories() ?: listOf()))
            else emit(States.failed(it.message ?: ""))
        }
    }.catch {
        Log.d(TAG, "getAllStory: failed = ${it.message}")
        emit(States.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}