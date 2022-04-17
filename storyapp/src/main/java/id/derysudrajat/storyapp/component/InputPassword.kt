package id.derysudrajat.storyapp.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import id.derysudrajat.storyapp.databinding.LayoutInputPasswordBinding

class InputPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutInputPasswordBinding by lazy {
        LayoutInputPasswordBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val isNotEmpty = MutableLiveData<Boolean>()

    fun isNotEmpty(onEvent: (isNotEmpty: Boolean) -> Unit) {
        isNotEmpty.observe(context as LifecycleOwner) { onEvent(it) }
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        binding.edtPassword.doAfterTextChanged { showError(it.toString()) }
    }

    private fun showError(text: String) {
        isNotEmpty.value = if (text.isBlank()) {
            enableError("Password must be not empty")
            false
        } else {
            if (text.length < 6) {
                enableError("Password must be at least 6 char")
                false
            } else {
                disableError()
                true
            }
        }
    }

    private fun disableError() {
        binding.tilPassword.isErrorEnabled = false
    }

    private fun enableError(message: String) {
        binding.tilPassword.apply {
            error = message
            isErrorEnabled = true
        }
    }
}