package id.derysudrajat.storyapp.component

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import id.derysudrajat.storyapp.R

class EditTextEmail : AppCompatEditText {

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

    private val isValid = MutableLiveData<Pair<String, Boolean>>()

    fun validateEmail(activity: Activity, onValidate: (Pair<String, Boolean>) -> Unit) {
        isValid.observe(activity as LifecycleOwner) { onValidate(it) }
    }

    private fun initializeView() {
        hint = context.getString(R.string.email)
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        doAfterTextChanged {
            val emailValid = emailValidation(it.toString())
            if (it.toString().isNotBlank()) isValid.value =
                Pair(if (emailValid) "" else context.getString(R.string.email_not_valid), emailValid)
            else isValid.value = Pair(context.getString(R.string.email_must_not_empty), false)
        }
    }

    private fun emailValidation(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}