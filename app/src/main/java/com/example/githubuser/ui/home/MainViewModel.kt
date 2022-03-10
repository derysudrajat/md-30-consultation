package com.example.githubuser.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.User
import com.example.githubuser.repo.network.service.ApiGithubConfig
import com.example.githubuser.toListUser
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _users = MutableLiveData<List<User>>()
    val user get() = _users

    fun getDataUserByUsername(username: String) = viewModelScope.launch {
        ApiGithubConfig.getApiService().searchUserByUsername(username).items?.let {
            _users.value = it.toListUser()
        }
    }
}

