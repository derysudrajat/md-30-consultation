package com.example.githubuser.ui.tabviewpagger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.githubuser.User
import com.example.githubuser.databinding.FragmentFollowingFollowersBinding
import com.example.githubuser.ui.home.ListUserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFollowersFragment : Fragment() {

    private var _binding: FragmentFollowingFollowersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FollowingFollowersViewModel by viewModels()

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
                    if (position == 0) viewModel.getDataFollowing(username)
                    else viewModel.getDataFollowers(username)
                }
            }
        }

        viewModel.users.observe(viewLifecycleOwner) { populateData(it) }
        viewModel.isLoading.observe(viewLifecycleOwner) { populateLoading(it) }

    }

    private fun populateLoading(it: Boolean) {

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