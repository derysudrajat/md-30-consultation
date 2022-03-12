package com.example.githubuser.ui.tabviewpagger

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(
    fa: FragmentActivity,
    private val username: String
) : FragmentStateAdapter(fa) {
    private val listFragment = listOf(
        FollowingFollowersFragment(),
        FollowingFollowersFragment()
    )

    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment = listFragment[position].apply {
        arguments = Bundle().apply {
            putString("username", username)
            putInt("position", position)
        }
    }

}