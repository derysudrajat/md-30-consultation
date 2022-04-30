package id.derysudrajat.storyapp.ui.register

import androidx.lifecycle.MutableLiveData
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

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    fun register(
        registerBody: RegisterBody,
        onRegistered: (isSuccess: Boolean, message: String) -> Unit
    ) = viewModelScope.launch {
        repository.register(registerBody).collect {
            when (it) {
                is States.Loading -> { _isLoading.value = true }
                is States.Success -> {
                    _isLoading.value = false
                    onRegistered(true, it.data)
                }
                is States.Failed -> {
                    _isLoading.value = false
                    onRegistered(false, it.message)
                }
            }
        }
    }
}