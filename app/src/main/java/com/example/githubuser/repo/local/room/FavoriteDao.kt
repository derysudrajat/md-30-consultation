package com.example.githubuser.repo.local.room

import androidx.room.*
import com.example.githubuser.repo.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE username = :username)")
    fun isFavorites(username: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun removeFromFavorite(favoriteEntity: FavoriteEntity)
}