package com.example.githubuser


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.databinding.UserDetailBinding


class UserDetail : AppCompatActivity() {

    private lateinit var binding: UserDetailBinding


    companion object {
        var EXTRA_DATA = "0"
        const val EXTRA_user = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableExtra<user>(EXTRA_user)?.let { user ->
            with(binding) {
                imgPhoto.setImageResource(user.avatar)
                detailName.text = user.name
                detailUsername.text = user.username
                detailRepository.text = user.repository
                detailFollowers.text = user.followers
                detailFollowing.text = user.following
                detailCompany.text = user.company
                detailLocation.text = user.location
            }
        }
    }
}



