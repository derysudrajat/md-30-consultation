package com.example.githubuser.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.User
import com.example.githubuser.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private var _users = MutableLiveData<List<User>>()
    val user get() = _users

    fun getDataUserByUsername(username: String) = viewModelScope.launch {
        userRepository.searchUser(username).collect { _users.value = it }
    }
}

