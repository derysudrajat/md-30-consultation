package id.derysudrajat.storyapp.component

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged

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


    private fun initializeView() {
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
        error = null
    }

    private fun showError(message: String) {
        error = message
    }
}