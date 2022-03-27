package com.example.githubuser.repo

import com.example.githubuser.data.Favorites
import com.example.githubuser.data.toFavoritesEntity
import com.example.githubuser.repo.local.LocalDataSource
import com.example.githubuser.repo.network.RemoteDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {
    fun searchUser(query: String) = remoteDataSource.searchUser(query)
    fun getDetailUser(username: String) = remoteDataSource.getDetailUser(username)
    fun getFollowers(username: String) = remoteDataSource.getFollowers(username)
    fun getFollowing(username: String) = remoteDataSource.getFollowing(username)

    fun getAllFavorites() = localDataSource.getAllFavorites()
    fun isFavorite(username: String) = localDataSource.isFavorite(username)
    suspend fun addToFavorite(favorites: Favorites) =
        localDataSource.addToFavorite(favorites.toFavoritesEntity())

    suspend fun deleteFromFavorite(favorites: Favorites) =
        localDataSource.removeFromFavorite(favorites.toFavoritesEntity())
}