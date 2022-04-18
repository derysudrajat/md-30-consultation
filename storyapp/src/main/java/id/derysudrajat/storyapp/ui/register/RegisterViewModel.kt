package id.derysudrajat.storyapp.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.derysudrajat.storyapp.repo.States
import id.derysudrajat.storyapp.repo.StoryRepository
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    fun register(
        registerBody: RegisterBody,
        onRegistered: (isSuccess: Boolean, message: String) -> Unit
    ) = viewModelScope.launch {
        repository.register(registerBody).collect {
            when (it) {
                is States.Loading -> {}
                is States.Success -> {
                    onRegistered(true, "Register Success: ${it.data}")
                }
                is States.Failed -> {
                    onRegistered(false, "Register Failed: ${it.message}")
                }
            }
        }
    }
}