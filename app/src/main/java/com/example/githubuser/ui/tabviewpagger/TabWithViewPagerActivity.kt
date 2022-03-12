package com.example.githubuser.ui.tabviewpagger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.databinding.ActivityTabWithViewPaggerBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabWithViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTabWithViewPaggerBinding

    companion object {
        private val TAB_TITLE = listOf(
            "Following", "Followers"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabWithViewPaggerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.getString("username")?.let {
            with(binding) {
                viewPager.adapter = ScreenSlidePagerAdapter(this@TabWithViewPagerActivity, it)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = TAB_TITLE[position]
                }.attach()
            }
        }

    }
}