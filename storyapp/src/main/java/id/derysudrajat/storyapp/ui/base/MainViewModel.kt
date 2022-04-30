package id.derysudrajat.storyapp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.StoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private lateinit var _stories: LiveData<PagingData<Story>>
    val stories get() = _stories

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    fun getAllStories(token: String) = viewModelScope.launch {
        _stories = repository.getAllStory(token)
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }


    private fun putDataToDatabase(story: List<Story>) {
        viewModelScope.launch {
            repository.deleteAllLocalStories()
        }.also {
            viewModelScope.launch {
                repository.addAllLocalStories(story)
            }
        }
    }
}