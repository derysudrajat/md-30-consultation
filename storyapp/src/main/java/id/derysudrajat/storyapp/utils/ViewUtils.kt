package id.derysudrajat.storyapp.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.data.model.LoginResult


object ViewUtils {

    const val authIcon =
        "https://www.kindpng.com/picc/m/269-2697881_computer-icons-user-clip-art-transparent-png-icon.png"
    val LoginResult.tokenBearer: String get() = "Bearer ${this.token}"

    val Context.isDarkMode get() = this.resources?.configuration?.uiMode?.and(UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES

    fun RecyclerView.buildSkeleton(context: Context, layout: Int, defaultSize: Int? = 10): Skeleton {
        val skeleton = this.applySkeleton(layout, defaultSize ?: 10)
        skeleton.apply {
            showShimmer = true
            shimmerColor = ContextCompat.getColor(context, R.color.gray)
            maskCornerRadius = 32f
        }
        return skeleton
    }

    fun View.buildSkeleton(context: Context): Skeleton {
        val skeleton = this.createSkeleton()
        skeleton.apply {
            showShimmer = true
            shimmerColor = ContextCompat.getColor(context, R.color.gray)
            maskCornerRadius = 32f
        }
        return skeleton
    }

    fun Activity.hideKeyboard() {
        val imm = ContextCompat.getSystemService(
            this, InputMethodManager::class.java
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
    }

}