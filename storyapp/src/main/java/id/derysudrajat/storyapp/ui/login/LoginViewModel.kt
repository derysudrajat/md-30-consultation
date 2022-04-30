package id.derysudrajat.storyapp.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.repo.States
import id.derysudrajat.storyapp.repo.StoryRepository
import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: StoryRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    fun login(context: Context, loginBody: LoginBody, onSuccess: (LoginResult) -> Unit) =
        viewModelScope.launch {
            repository.login(loginBody).collect {
                when (it) {
                    is States.Loading -> { _isLoading.value = true}
                    is States.Success -> {
                        _isLoading.value = false
                        Toast.makeText(
                            context, context.getString(R.string.welcome_messages, it.data.name), Toast.LENGTH_SHORT
                        ).show()
                        onSuccess(it.data)
                    }
                    is States.Failed -> {
                        _isLoading.value = false
                        Toast.makeText(
                            context, context.getString(R.string.login_failed, it.message), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
}