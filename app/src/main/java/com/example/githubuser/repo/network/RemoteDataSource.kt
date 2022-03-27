package com.example.githubuser.repo.network

import android.util.Log
import com.example.githubuser.User
import com.example.githubuser.repo.network.service.GithubService
import com.example.githubuser.toListUser
import com.example.githubuser.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val githubService: GithubService
) {
    fun searchUser(query: String) = flow<List<User>> {
        githubService.searchUserByUsername(query).items.let {
            if (it != null) emit(it.toListUser()) else emit(listOf())
        }
    }.catch {
        Log.d("TAG", "searchUser: failed = ${it.message}")
    }.flowOn(Dispatchers.IO)

    fun getDetailUser(username: String) = flow {
        emit(githubService.getDetailUser(username).toUser())
    }.catch {
        Log.d("TAG", "getDetailUser: failed = ${it.message}")
    }.flowOn(Dispatchers.IO)

    fun getFollowers(username: String) = flow<List<User>> {
        emit(githubService.getFollowers(username).toListUser())
    }.catch {
        Log.d("TAG", "getFollowers: failed = $it")
    }.flowOn(Dispatchers.IO)

    fun getFollowing(username: String) = flow<List<User>> {
        emit(githubService.getFollowing(username).toListUser())
    }.catch {
        Log.d("TAG", "getFollowing: failed = $it")
    }.flowOn(Dispatchers.IO)

}