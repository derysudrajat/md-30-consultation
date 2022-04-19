package id.derysudrajat.storyapp.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.derysudrajat.storyapp.repo.States
import id.derysudrajat.storyapp.repo.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    fun postNewStory(
        token: String,
        file: MultipartBody.Part,
        desc: String,
        onStoryPosted: (isPosted: Boolean, message: String) -> Unit
    ) {
        viewModelScope.launch {
            val description = desc.toRequestBody("text/plain".toMediaType())
            repository.postNewStory(token, file, description).collect {
                when (it) {
                    is States.Loading -> {}
                    is States.Success -> onStoryPosted(true, it.data)
                    is States.Failed -> onStoryPosted(false, it.message)
                }
            }
        }
    }
}