package com.example.githubuser.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.User
import com.example.githubuser.data.toListUser
import com.example.githubuser.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _favorites = MutableLiveData<List<User>>()
    val favorites get() = _favorites

    fun getAllFavorites() = viewModelScope.launch {
        userRepository.getAllFavorites().collect {
            _favorites.value = it.toListUser()
        }
    }
}