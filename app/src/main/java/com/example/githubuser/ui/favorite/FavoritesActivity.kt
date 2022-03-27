package com.example.githubuser.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.User
import com.example.githubuser.databinding.ActivityFavoritesBinding
import com.example.githubuser.ui.detail.UserDetail
import com.example.githubuser.ui.home.ListUserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getAllFavorites()

        viewModel.favorites.observe(this) { showRecyclerList(it) }
    }

    private fun showRecyclerList(listUser: List<User>) {
        val listUserAdapter = ListUserAdapter(listUser)
        listUserAdapter.setOnItemClickCallback(onItemCallBack)

        binding.rvFavorites.apply {
            setHasFixedSize(true)
            layoutManager =
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    GridLayoutManager(this@FavoritesActivity, 2)
                else LinearLayoutManager(this@FavoritesActivity)
            adapter = listUserAdapter
        }
    }

    private val onItemCallBack = object : ListUserAdapter.OnItemClickCallback {
        override fun onItemClicked(data: User) {
            toDetailActivity(data)
        }
    }

    private fun toDetailActivity(data: User) {
        startActivity(
            Intent(this, UserDetail::class.java).apply {
                putExtra(UserDetail.EXTRA_user, data)
            }
        )
    }
}