package com.example.githubuser.ui.detail


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.User
import com.example.githubuser.databinding.UserDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetail : AppCompatActivity() {

    private lateinit var binding: UserDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var isFav = false

    companion object {
        var EXTRA_DATA = "0"
        const val EXTRA_user = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.getParcelable<User>(EXTRA_user)?.let { user ->
            detailViewModel.getDetailUser(user.username)
            detailViewModel.isFavorite(user.username)

            binding.btnFavorite.setOnClickListener {
                if (isFav) detailViewModel.removeFromFavorite(user)
                else detailViewModel.addToFavorite(user)
            }
        }

        detailViewModel.user.observe(this) {
            populateDataUser(it)
        }
        detailViewModel.isFavorite.observe(this) {
            isFav = it
            populateFavorite(it)
        }
    }

    private fun populateFavorite(isFavorite: Boolean) {
        binding.btnFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite_outline
        )
    }

    private fun populateDataUser(user: User) {
        with(binding) {
            Glide.with(this@UserDetail).load(user.avatar).into(imgPhoto)
            detailName.text = user.name
            detailUsername.text = user.username
            detailRepository.text = user.repository.toString()
            detailFollowers.text = user.followers.toString()
            detailFollowing.text = user.following.toString()
            detailCompany.text = user.company
            detailLocation.text = user.location
        }
    }
}



