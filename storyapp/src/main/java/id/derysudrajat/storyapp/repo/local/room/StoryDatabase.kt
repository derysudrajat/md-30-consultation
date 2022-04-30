package id.derysudrajat.storyapp.repo.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.derysudrajat.storyapp.repo.local.entity.StoryEntity


@Database(entities = [StoryEntity::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        fun getInstance(context: Context): StoryDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    "Story.db"
                ).fallbackToDestructiveMigration().build().apply { INSTANCE = this }
            }
    }
}