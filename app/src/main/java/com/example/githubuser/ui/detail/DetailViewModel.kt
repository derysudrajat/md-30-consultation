package com.example.githubuser.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.User
import com.example.githubuser.repo.UserRepository
import com.example.githubuser.toFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _user = MutableLiveData<User>()
    val user get() = _user
    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite get() = _isFavorite

    fun getDetailUser(username: String) = viewModelScope.launch {
        userRepository.getDetailUser(username).collect { _user.value = it }
    }

    fun isFavorite(username: String) = viewModelScope.launch {
        userRepository.isFavorite(username).collect {
            _isFavorite.value = it
        }
    }

    fun addToFavorite(user: User) = viewModelScope.launch {
        userRepository.addToFavorite(user.toFavorite())
    }

    fun removeFromFavorite(user: User) = viewModelScope.launch {
        userRepository.deleteFromFavorite(user.toFavorite())
    }
}