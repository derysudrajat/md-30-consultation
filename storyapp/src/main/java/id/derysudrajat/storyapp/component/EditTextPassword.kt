package id.derysudrajat.storyapp.component

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class EditTextPassword : AppCompatEditText {
    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView()
    }

    private val hideError = MutableLiveData<Boolean>()
    private val showError = MutableLiveData<String>()

    fun onValidateEditText(
        activity: Activity,
        hideError: () -> Unit,
        showError: (message: String) -> Unit
    ) {
        this.hideError.observe(activity as LifecycleOwner) { hideError() }
        this.showError.observe(activity as LifecycleOwner) { showError(it) }
    }


    private fun initializeView() {
        hint = "Password"
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = PasswordTransformationMethod()
        doAfterTextChanged {
            if (it?.isBlank() == true) showError("Must be not empty") else {
                if ((it?.length
                        ?: 0) < 6
                ) showError("Password must be at least 6 char") else hideError()
            }
        }
    }

    private fun hideError() {
        hideError.value = true
    }

    private fun showError(message: String) {
        showError.value = message
    }
}