package com.example.githubuser.ui.tabviewpagger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.User
import com.example.githubuser.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingFollowersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _users = MutableLiveData<List<User>>()
    val users get() = _users

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    fun getDataFollowers(username: String) = viewModelScope.launch {
        _isLoading.value = true
        userRepository.getFollowers(username).collect {
            _users.value = it
            _isLoading.value = false
        }
    }

    fun getDataFollowing(username: String) = viewModelScope.launch {
        _isLoading.value = true
        userRepository.getFollowing(username).collect {
            _users.value = it
            _isLoading.value = false
        }
    }
}