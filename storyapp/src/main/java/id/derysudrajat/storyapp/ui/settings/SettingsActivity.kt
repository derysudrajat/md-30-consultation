package id.derysudrajat.storyapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.databinding.ActivitySettingsBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.ui.login.LoginActivity
import id.derysudrajat.storyapp.utils.DataHelpers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var localStore: LocalStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            localStore.getUserLoginResult().collect {
                binding.ivAvatar.load(DataHelpers.authIcon) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                binding.tvUserName.text = it.name
            }
        }
        binding.toolbar.setToolbar(
            getString(R.string.settings),
            titleAlignment = View.TEXT_ALIGNMENT_TEXT_START
        )
        binding.toolbar.setBack(this) { onBackPressed() }


        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                localStore.putLoginResult(LoginResult("", "", ""))
                    .also {
                        startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                        finish()
                    }
            }
        }
    }


}