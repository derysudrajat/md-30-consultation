package com.example.githubuser.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.User
import com.example.githubuser.repo.network.service.ApiGithubConfig
import com.example.githubuser.toUser
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private var _user = MutableLiveData<User>()
    val user get() = _user

    fun getDetailUser(username: String) = viewModelScope.launch {
        ApiGithubConfig.getApiService().getDetailUser(username).let {
            _user.value = it.toUser()
        }
    }
}