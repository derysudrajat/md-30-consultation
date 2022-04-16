package id.derysudrajat.storyapp.component

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class InputPassword : TextInputLayout {
    private lateinit var editText: TextInputEditText
    val isNotEmpty = MutableLiveData<Boolean>()

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView()
    }


    private fun initializeView() {
        editText = TextInputEditText(context)
        createEditBox(editText)
        editText.doAfterTextChanged { showError(it.toString()) }
    }

    private fun createEditBox(editText: TextInputEditText) {
        val layoutParams: LinearLayout.LayoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        this.layoutParams = layoutParams
        this.boxBackgroundMode = BOX_BACKGROUND_OUTLINE
        this.hint = "Password"
        this.boxBackgroundColor = Color.WHITE
        this.setBoxCornerRadii(8f, 8f, 8f, 8f)

        editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        editText.transformationMethod = PasswordTransformationMethod()

        editText.layoutParams = layoutParams
        addView(editText)
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
        this.isErrorEnabled = false
    }

    private fun enableError(message: String) {
        this.apply {
            error = message
            isErrorEnabled = true
        }
    }
}