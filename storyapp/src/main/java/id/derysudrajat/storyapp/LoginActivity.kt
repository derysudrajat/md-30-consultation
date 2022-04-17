package id.derysudrajat.storyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import coil.load
import coil.transform.CircleCropTransformation
import id.derysudrajat.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val isValid = mutableListOf(false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.load("https://www.kindpng.com/picc/m/269-2697881_computer-icons-user-clip-art-transparent-png-icon.png") {
            transformations(CircleCropTransformation())
        }

        binding.inputPassword.isNotEmpty {
            isValid[1] = it
            validateButton()
        }

        binding.edtEmail.doAfterTextChanged {
            isValid[0] = it?.isNotBlank() ?: false
            validateButton()
        }
        binding.btnLogin.setOnClickListener {
            // login
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.edtCustomPassword.onValidateEditText(
            activity = this,
            hideError = { binding.tilCustomPassword.isErrorEnabled = false },
            showError = {
                binding.tilCustomPassword.apply {
                    error = it
                    isErrorEnabled = true
                }
            }
        )

    }

    private fun validateButton() {
        binding.btnLogin.isEnabled = isValid.filter { it }.size == 2
    }
}