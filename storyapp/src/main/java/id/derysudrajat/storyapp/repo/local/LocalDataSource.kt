package id.derysudrajat.storyapp.repo.local

import android.util.Log
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.local.entity.StoryEntity
import id.derysudrajat.storyapp.repo.local.entity.toStories
import id.derysudrajat.storyapp.repo.local.room.StoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val storyDao: StoryDao
) {
    fun getAllStories() = flow<List<Story>> {
        storyDao.getAllStories().collect { emit(it.toStories()) }
    }.catch {
        Log.d("TAG", "getAllFavorites: ")
    }.flowOn(Dispatchers.IO)

    suspend fun addAllStories(stories: List<StoryEntity>) = storyDao.addAllStories(stories)

    suspend fun deleteAllStories() = storyDao.deleteAll()
}