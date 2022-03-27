package com.example.githubuser.repo.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.repo.local.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: GithubDatabase? = null

        fun getInstance(context: Context): GithubDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    GithubDatabase::class.java,
                    "Github.db"
                ).fallbackToDestructiveMigration().build().apply { INSTANCE = this }
            }
    }
}