package id.derysudrajat.storyapp.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
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

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    fun postNewStory(
        token: String,
        file: MultipartBody.Part,
        desc: String,
        latLng: LatLng,
        onStoryPosted: (isPosted: Boolean, message: String) -> Unit
    ) {
        viewModelScope.launch {
            val description = desc.toRequestBody("text/plain".toMediaType())
            repository.postNewStory(token, file, description, latLng).collect {
                when (it) {
                    is States.Loading -> { _isLoading.value = true }
                    is States.Success -> {
                        _isLoading.value = false
                        onStoryPosted(true, it.data)
                    }
                    is States.Failed -> {
                        _isLoading.value = false
                        onStoryPosted(false, it.message)
                    }
                }
            }
        }
    }
}