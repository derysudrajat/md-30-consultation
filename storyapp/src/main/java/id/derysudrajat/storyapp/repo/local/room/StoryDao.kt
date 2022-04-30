package id.derysudrajat.storyapp.repo.local.room

import androidx.room.*
import id.derysudrajat.storyapp.repo.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {

    @Query("SELECT * FROM stories")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllStories(favoriteEntity: List<StoryEntity>)

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}
