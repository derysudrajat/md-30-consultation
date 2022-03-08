package com.example.githubuser

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private val list = ArrayList<user>()
    var isCurrentThemeDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        isCurrentThemeDarkMode = getCurrentTheme()
        setAppTheme(isCurrentThemeDarkMode)
        setContentView(binding.root)
        movieViewModel.getNowPlayingMovie()

        list.addAll(listUser)
        showRecyclerList()
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        setDefaultNightMode(if (isDarkMode) MODE_NIGHT_NO else MODE_NIGHT_YES)
    }

    private val listUser: ArrayList<user>
        get() {
            val dataName = resources.getStringArray(R.array.name)
            val dataUserName = resources.getStringArray(R.array.username)
            val dataPhoto = resources.obtainTypedArray(R.array.avatar)
            val dataFollowers = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val listUser = ArrayList<user>()
            for (i in dataName.indices) {
                val user = user(
                    name = dataName[i],
                    username = dataUserName[i],
                    avatar = dataPhoto.getResourceId(i, -1),
                    followers = dataFollowers[i],
                    following = dataFollowing[i],
                    company = dataCompany[i],
                    location = dataLocation[i],
                    repository = dataRepository[i]
                )
                listUser.add(user)
            }
            return listUser
        }

    private fun showRecyclerList() {
        val listUserAdapter = ListUserAdapter(list)
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
        return super.onCreateOptionsMenu(menu)
    }

    private val onItemCallBack = object : ListUserAdapter.OnItemClickCallback {
        override fun onItemClicked(data: user) {
            startActivity(
                Intent(this@MainActivity, UserDetail::class.java).apply {
                    putExtra(UserDetail.EXTRA_user, data)
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_theme) {
            isCurrentThemeDarkMode = !isCurrentThemeDarkMode
            putDataTheme(isCurrentThemeDarkMode)
            setAppTheme(isCurrentThemeDarkMode)
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


