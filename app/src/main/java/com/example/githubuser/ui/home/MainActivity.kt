package com.example.githubuser.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.MovieViewModel
import com.example.githubuser.R
import com.example.githubuser.User
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.detail.UserDetail
import com.example.githubuser.ui.tabviewpagger.TabWithViewPagerActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    var isCurrentThemeDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        isCurrentThemeDarkMode = getCurrentTheme()
        setAppTheme(isCurrentThemeDarkMode)
        setContentView(binding.root)
        movieViewModel.getNowPlayingMovie()
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
                setAppTheme(isCurrentThemeDarkMode)
            }
            R.id.action_following_followers -> {
                toFollowingFollowersActivity("derysudrajat")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val THEME_ARG = "theme_arg"
    }

    private fun putDataTheme(isDarkMode: Boolean) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(THEME_ARG, isDarkMode)
            apply()
        }
    }

    private fun getCurrentTheme(): Boolean {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultValue = false
        return sharedPref.getBoolean(THEME_ARG, defaultValue)
    }

}


