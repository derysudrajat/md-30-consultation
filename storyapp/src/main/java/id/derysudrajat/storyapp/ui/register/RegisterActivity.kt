package id.derysudrajat.storyapp.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import coil.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.databinding.ActivityRegisterBinding
import id.derysudrajat.storyapp.repo.remote.body.RegisterBody
import id.derysudrajat.storyapp.utils.DataHelpers

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val isValid = mutableListOf(false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            toolbar.setToolbar(getString(R.string.register))
            imageView.load(DataHelpers.authIcon) {
                transformations(CircleCropTransformation())
            }
            edtName.doAfterTextChanged {
                val nameValid = it?.isNotBlank() ?: false
                isValid[0] = nameValid
                tilName.apply {
                    if (!nameValid) error = context.getString(R.string.name_must_not_empty)
                    isErrorEnabled = !nameValid
                }
                validateButton()
            }
            edtEmail.validateEmail(this@RegisterActivity) {
                isValid[1] = it.second
                if (!it.second) binding.tilEmail.apply {
                    error = it.first
                    isErrorEnabled = true
                } else binding.tilEmail.isErrorEnabled = false
                validateButton()
            }
            inputPassword.isNotEmpty {
                isValid[2] = it
                validateButton()
            }

            tvRegister.setOnClickListener { finish() }
            btnRegister.setOnClickListener {
                viewModel.register(
                    RegisterBody(
                        inputPassword.text,
                        edtName.text.toString(),
                        edtEmail.text.toString()
                    ), ::onRegistered
                )
            }
        }
    }

    private fun onRegistered(isSuccess: Boolean, message: String) {
        Toast.makeText(
            this, getString(
                if (isSuccess) R.string.register_success else R.string.register_failed, message
            ), Toast.LENGTH_SHORT
        ).show()
        if (isSuccess) finish()
    }

    private fun validateButton() {
        binding.btnRegister.isEnabled = isValid.filter { it }.size == 3
    }
}