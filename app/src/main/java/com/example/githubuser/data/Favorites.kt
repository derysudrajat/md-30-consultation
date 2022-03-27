package com.example.githubuser.data

import com.example.githubuser.User
import com.example.githubuser.repo.local.entity.FavoriteEntity

data class Favorites(
    val id: Int,
    val username: String,
    val imgAvatar: String,
)

fun Favorites.toFavoritesEntity() = FavoriteEntity(this.id, this.username, this.imgAvatar)
fun Favorites.toUser() = User(
    id = this.id, username = this.username, avatar = this.imgAvatar, name = "",
    company = "", location = "", repository = 0, followers = 0, following = 0
)

fun List<Favorites>.toListUser(): MutableList<User> {
    val listUser = mutableListOf<User>()
    this.forEach { listUser.add(it.toUser()) }
    return listUser
}
