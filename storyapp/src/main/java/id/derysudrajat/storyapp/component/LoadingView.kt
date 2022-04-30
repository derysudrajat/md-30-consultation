package id.derysudrajat.storyapp.component

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import id.derysudrajat.storyapp.databinding.LayoutDialogBinding
import id.derysudrajat.storyapp.utils.ViewUtils.hideKeyboard

class LoadingView constructor(private val activity: Activity) {
    private lateinit var currentDialog: Dialog

    fun showLoading(title: String) {
        val binding = LayoutDialogBinding.inflate(activity.layoutInflater)
        val builder = AlertDialog.Builder(activity)
        val dialog = builder.setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.textView.text = title
        activity.hideKeyboard()
        currentDialog = dialog
        dialog.show()
    }

    fun dismissLoading() = currentDialog.dismiss()

    companion object {
        fun create(activity: Activity) = LoadingView(activity)
    }
}