package id.derysudrajat.storyapp.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.databinding.LayoutToolbarBinding
import id.derysudrajat.storyapp.utils.ViewUtils.isDarkMode

class StoryToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutToolbarBinding by lazy {
        LayoutToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setBack(activity: AppCompatActivity, onBack: () -> Unit) {
        activity.setSupportActionBar(binding.mToolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mToolbar.setNavigationOnClickListener { onBack() }
    }

    fun setMenu(menuId: Int, onMenuClicked: (items: MenuItem) -> Unit){
        binding.mToolbar.inflateMenu(menuId)
        binding.mToolbar.setOnMenuItemClickListener {
            onMenuClicked(it)
            true
        }
    }

    fun setToolbar(
        title: String,
        subtitle: String? = null,
        titleAlignment: Int? = View.TEXT_ALIGNMENT_CENTER,
        showIcon: Boolean? = false
    ) {
        with(binding) {
            tvTitle.apply {
                text = title
                textAlignment = titleAlignment ?: View.TEXT_ALIGNMENT_CENTER
            }
            subtitle?.let {
                tvSubtitle.apply {
                    text = it
                    textAlignment = titleAlignment ?: View.TEXT_ALIGNMENT_CENTER
                    visibility = View.VISIBLE
                }
            }
            showIcon?.let {
                ivIcon.visibility = if (it) View.VISIBLE else View.GONE
                ivIcon.setImageResource(if (context.isDarkMode) R.drawable.ic_story_white else R.drawable.ic_story)
            }
        }
    }
}