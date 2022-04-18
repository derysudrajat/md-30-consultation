package id.derysudrajat.storyapp.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import id.derysudrajat.storyapp.databinding.LayoutToolbarBinding

class StoryToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutToolbarBinding by lazy {
        LayoutToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setToolbar(
        title: String,
        titleAlignment: Int? = View.TEXT_ALIGNMENT_CENTER,
        showIcon: Boolean? = false
    ) {
        with(binding) {
            tvTitle.apply {
                text = title
                textAlignment = titleAlignment ?: View.TEXT_ALIGNMENT_CENTER
            }
            showIcon?.let {
                ivIcon.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }
}