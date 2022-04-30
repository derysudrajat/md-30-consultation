package id.derysudrajat.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.component.LoadingView
import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.databinding.ActivityLoginBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.repo.remote.body.LoginBody
import id.derysudrajat.storyapp.ui.base.MainActivity
import id.derysudrajat.storyapp.ui.register.RegisterActivity
import id.derysudrajat.storyapp.utils.ViewUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var localStore: LocalStore
    private val isValid = mutableListOf(false, false)
    private lateinit var loadingView: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingView = LoadingView.create(this)
        binding.imageView.load(ViewUtils.authIcon) {
            transformations(CircleCropTransformation())
        }

        binding.toolbar.setToolbar(getString(R.string.login))

        binding.edtPassword.onValidateEditText(
            activity = this,
            hideError = {
                binding.tilPassword.isErrorEnabled = false
                isValid[1] = true
                validateButton()
            },
            showError = {
                binding.tilPassword.apply {
                    error = it
                    isErrorEnabled = true
                    isValid[1] = false
                    validateButton()
                }
            }
        )
        binding.edtEmail.validateEmail(this) {
            isValid[0] = it.second
            if (!it.second) binding.tilEmail.apply {
                error = it.first
                isErrorEnabled = true
            } else binding.tilEmail.isErrorEnabled = false
            validateButton()
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                this, LoginBody(
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString()
                ), this::putLoginResult
            )
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.isLoading.observe(this, ::populateLoading)

    }

    private fun populateLoading(isLoading: Boolean) {
        if (isLoading) loadingView.showLoading(getString(R.string.try_to_login)) else loadingView.dismissLoading()
    }

    private fun putLoginResult(loginResult: LoginResult) {
        lifecycleScope.launch {
            localStore.putLoginResult(loginResult).also {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

    }

    private fun validateButton() {
        binding.btnLogin.isEnabled = isValid.filter { it }.size == 2
    }
}