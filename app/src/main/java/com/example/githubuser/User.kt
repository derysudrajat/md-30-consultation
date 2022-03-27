package com.example.githubuser

import android.os.Parcelable
import com.example.githubuser.data.Favorites
import com.example.githubuser.repo.network.response.UserResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var username: String,
    var name: String,
    var avatar: String,
    var company: String,
    var location: String,
    var repository: Int,
    var followers: Int,
    var following: Int
) : Parcelable

fun User.toFavorite() = Favorites(this.id, this.username, this.avatar)


fun List<UserResponse>.toListUser(): MutableList<User> {
    val listUser = mutableListOf<User>()
    this.forEach { listUser.add(it.toUser()) }
    return listUser
}

fun UserResponse.toUser(): User {
    return User(
        id = this.id ?: 0,
        username = this.login ?: "",
        avatar = this.avatarUrl ?: "",
        name = this.name ?: "",
        company = this.company ?: "-",
        location = this.location ?: "",
        repository = this.publicRepos ?: 0,
        following = this.following ?: 0,
        followers = this.followers ?: 0
    )
}