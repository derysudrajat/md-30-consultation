package com.example.githubuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.toListMovie
import com.example.githubuser.repo.network.service.ApiMovieConfig
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    fun getNowPlayingMovie() = viewModelScope.launch {
        ApiMovieConfig.getApiService().getNowPlayingMovies().let {
            it.movies?.let { moviesResponse ->
                moviesResponse.forEachIndexed { index, responseMovie ->
                    Log.d(
                        "\nMovieViewModel",
                        "[NullAble]getNowPlayingMovie-$index: \n$responseMovie"
                    )
                }
            }
            it.movies?.toListMovie()?.let { movies ->
                movies.forEachIndexed { index, movie ->
                    Log.d("\nMovieViewModel", "[Non-Null]getNowPlayingMovie-$index: \n$movie")
                }
            }
        }
    }
}