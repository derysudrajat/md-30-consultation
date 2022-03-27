package com.example.githubuser.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.User
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.repo.local.LocalShared
import com.example.githubuser.ui.detail.UserDetail
import com.example.githubuser.ui.favorite.FavoritesActivity
import com.example.githubuser.ui.tabviewpagger.TabWithViewPagerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    var isCurrentThemeDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        getCurrentTheme()
        setAppTheme(isCurrentThemeDarkMode)
        setContentView(binding.root)
        mainViewModel.getDataUserByUsername("dery")
        mainViewModel.user.observe(this) { showRecyclerList(it) }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        setDefaultNightMode(if (isDarkMode) MODE_NIGHT_NO else MODE_NIGHT_YES)
    }

    private fun showRecyclerList(listUser: List<User>) {
        val listUserAdapter = ListUserAdapter(listUser)
        listUserAdapter.setOnItemClickCallback(onItemCallBack)

        binding.rvAvatar.apply {
            setHasFixedSize(true)
            layoutManager =
                if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    GridLayoutManager(this@MainActivity, 2)
                else LinearLayoutManager(this@MainActivity)
            adapter = listUserAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { mainViewModel.getDataUserByUsername(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private val onItemCallBack = object : ListUserAdapter.OnItemClickCallback {
        override fun onItemClicked(data: User) {
            toDetailActivity(data)
        }
    }

    private fun toFollowingFollowersActivity(username: String) {
        startActivity(
            Intent(this, TabWithViewPagerActivity::class.java).apply {
                putExtra("username", username)
            }
        )
    }

    private fun toDetailActivity(data: User) {
        startActivity(
            Intent(this@MainActivity, UserDetail::class.java).apply {
                putExtra(UserDetail.EXTRA_user, data)
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_theme -> {
                isCurrentThemeDarkMode = !isCurrentThemeDarkMode
                putDataTheme(isCurrentThemeDarkMode)
            }
            R.id.action_following_followers -> {
                toFollowingFollowersActivity("derysudrajat")
            }
            R.id.action_favorite -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun putDataTheme(isDarkMode: Boolean) {
        lifecycleScope.launch { LocalShared.updateThemes(this@MainActivity, isDarkMode) }
    }

    private fun getCurrentTheme() {
        lifecycleScope.launch {
            LocalShared.getThemes(this@MainActivity).collect {
                isCurrentThemeDarkMode = it
                setAppTheme(it)
            }
        }
    }

}


