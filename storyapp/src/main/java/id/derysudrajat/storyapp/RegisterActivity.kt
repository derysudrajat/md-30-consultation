package id.derysudrajat.storyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import coil.load
import coil.transform.CircleCropTransformation
import id.derysudrajat.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val isValid = mutableListOf(false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            imageView.load("https://www.kindpng.com/picc/m/269-2697881_computer-icons-user-clip-art-transparent-png-icon.png") {
                transformations(CircleCropTransformation())
            }
            edtName.doAfterTextChanged {
                isValid[0] = it?.isNotBlank() ?: false
                validateButton()
            }
            edtEmail.doAfterTextChanged {
                isValid[1] = it?.isNotBlank() ?: false
                validateButton()
            }
            inputPassword.isNotEmpty.observe(this@RegisterActivity) {
                isValid[2] = it
                validateButton()
            }

            tvRegister.setOnClickListener { finish() }
            btnRegister.setOnClickListener {
                // register
            }
        }
    }

    private fun validateButton() {
        binding.btnRegister.isEnabled = isValid.filter { it }.size == 3
    }
}