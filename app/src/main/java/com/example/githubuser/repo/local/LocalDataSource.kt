package com.example.githubuser.repo.local

import android.util.Log
import com.example.githubuser.data.Favorites
import com.example.githubuser.repo.local.entity.FavoriteEntity
import com.example.githubuser.repo.local.entity.toListFavorites
import com.example.githubuser.repo.local.room.FavoriteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    fun getAllFavorites() = flow<List<Favorites>> {
        favoriteDao.getAllFavorites().collect { emit(it.toListFavorites()) }
    }.catch {
        Log.d("TAG", "getAllFavorites: ")
    }.flowOn(Dispatchers.IO)

    fun isFavorite(username: String) = flow {
        favoriteDao.isFavorites(username).collect { emit(it) }
    }.catch {
        Log.d("TAG", "isFavorite: ")
    }.flowOn(Dispatchers.IO)

    suspend fun addToFavorite(favoriteEntity: FavoriteEntity) =
        favoriteDao.addToFavorite(favoriteEntity)

    suspend fun removeFromFavorite(favoriteEntity: FavoriteEntity) =
        favoriteDao.removeFromFavorite(favoriteEntity)
}