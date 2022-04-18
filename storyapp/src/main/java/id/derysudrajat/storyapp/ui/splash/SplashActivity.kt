package id.derysudrajat.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.databinding.ActivitySplashBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.ui.base.MainActivity
import id.derysudrajat.storyapp.ui.login.LoginActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var localStore: LocalStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(mainLooper).postDelayed({
            lifecycleScope.launch {
                localStore.getUserLoginResult().collect {
                    val target =
                        if (it.token.isBlank()) LoginActivity::class.java else MainActivity::class.java
                    startActivity(Intent(this@SplashActivity, target))
                }
            }
        }, 2000L)
    }
}