package id.derysudrajat.storyapp.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.States
import id.derysudrajat.storyapp.repo.StoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private var _stories = MutableLiveData<List<Story>>()
    val stories get() = _stories

    fun getAllStories(token: String) = viewModelScope.launch {
        repository.getAllStory(token).collect {
            when (it) {
                is States.Loading -> {}
                is States.Success -> {
                    it.data.let { story -> _stories.value = story }
                }
                is States.Failed -> {
                    Log.d("TAG", "getAllStories: failed = $it")
                }
            }
        }
    }
}