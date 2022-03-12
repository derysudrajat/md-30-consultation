package com.example.githubuser.ui.tabviewpagger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.githubuser.User
import com.example.githubuser.databinding.FragmentFollowingFollowersBinding
import com.example.githubuser.repo.network.service.ApiGithubConfig
import com.example.githubuser.toListUser
import com.example.githubuser.ui.home.ListUserAdapter
import kotlinx.coroutines.launch

class FollowingFollowersFragment : Fragment() {

    private var _binding: FragmentFollowingFollowersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            getInt("position").let { position ->
                getString("username")?.let { username ->
                    if (position == 0) getDataFollowing(username) else getDataFollowers(username)
                }
            }
        }

    }

    private fun getDataFollowers(username: String) = lifecycleScope.launch {
        ApiGithubConfig.getApiService().getFollowers(username).let {
            populateData(it.toListUser())
        }
    }

    private fun getDataFollowing(username: String) = lifecycleScope.launch {
        ApiGithubConfig.getApiService().getFollowing(username).let {
            populateData(it.toListUser())
        }
    }

    private fun populateData(listUser: List<User>) {
        binding.rvFollowingFollowers.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = ListUserAdapter(listUser)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}