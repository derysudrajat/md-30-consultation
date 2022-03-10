package com.example.githubuser

import android.os.Parcelable
import com.example.githubuser.repo.network.response.UserResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String,
    var name: String,
    var avatar: String,
    var company: String,
    var location: String,
    var repository: Int,
    var followers: Int,
    var following: Int
) : Parcelable


fun List<UserResponse>.toListUser(): MutableList<User> {
    val listUser = mutableListOf<User>()
    this.forEach { listUser.add(it.toUser()) }
    return listUser
}

fun UserResponse.toUser(): User {
    return User(
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