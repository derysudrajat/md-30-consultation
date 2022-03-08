package com.example.githubuser.data

import android.os.Parcelable
import com.example.githubuser.repo.network.response.ResponseMovie
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val overview: String,
    val originalLanguage: String,
    val originalTitle: String,
    val video: Boolean,
    val title: String,
    val genreIds: List<Int>,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val id: Int,
    val adult: Boolean,
    val voteCount: Int
) : Parcelable

fun List<ResponseMovie>.toListMovie(): MutableList<Movie> {
    val listMovie = mutableListOf<Movie>()
    this.forEach {
        listMovie.add(
            Movie(
                it.overview ?: "-",
                it.originalLanguage ?: "-",
                it.originalTitle ?: "-",
                it.video ?: false,
                it.title ?: "-",
                it.genreIds ?: listOf(),
                it.posterPath ?: "",
                it.backdropPath ?: "",
                it.releaseDate ?: "-",
                it.popularity ?: 0.0,
                it.voteAverage ?: 0.0,
                it.id ?: -1,
                it.adult ?: false,
                it.voteCount ?: 0
            )
        )
    }
    return listMovie
}
