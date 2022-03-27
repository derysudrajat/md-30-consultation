package com.example.githubuser.repo.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubuser.data.Favorites

@Entity(tableName = "favorites")
class FavoriteEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "img")
    val imgAvatar: String,
)

fun FavoriteEntity.toFavorite() = Favorites(this.id, this.username, this.imgAvatar)

fun List<FavoriteEntity>.toListFavorites(): MutableList<Favorites> {
    val listFavorites = mutableListOf<Favorites>()
    this.forEach { listFavorites.add(it.toFavorite()) }
    return listFavorites
}