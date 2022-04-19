package id.derysudrajat.storyapp.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.databinding.ActivityAddStoryBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.ui.camera.CameraActivity
import id.derysudrajat.storyapp.utils.DataHelpers
import id.derysudrajat.storyapp.utils.DataHelpers.reduceFileImage
import id.derysudrajat.storyapp.utils.DataHelpers.rotateBitmap
import id.derysudrajat.storyapp.utils.DataHelpers.tokenBearer
import id.derysudrajat.storyapp.utils.DataHelpers.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    @Inject
    lateinit var localStore: LocalStore
    private var currentToken = ""
    private val viewModel: AddStoryViewModel by viewModels()

    private var getFile: File? = null
    private val isPostValid = mutableListOf(false, false)

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        validateButton()

        lifecycleScope.launch {
            localStore.getUserLoginResult().collect {
                binding.tvUserName.text = it.name
                currentToken = it.tokenBearer
            }
        }

        binding.toolbar.apply {
            setToolbar(
                getString(R.string.add_story), titleAlignment = View.TEXT_ALIGNMENT_TEXT_START
            )
            setBack(this@AddStoryActivity) { onBackPressed() }
        }

        binding.ivAvatar.load(DataHelpers.authIcon) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.edtDesc.doAfterTextChanged {
            isPostValid[0] = it.toString().isNotBlank()
            validateButton()
        }

        binding.btnPickImage.setOnClickListener { startGallery() }
        binding.btnTakeCamera.setOnClickListener { startCameraX() }
        binding.btnPost.setOnClickListener { postNewStory() }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.ivPost.load(result) {
                transformations(RoundedCornersTransformation(8f))
            }
            binding.cardImage.isVisible = true
            isPostValid[1] = true
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.ivPost.load(selectedImg) {
                transformations(RoundedCornersTransformation(8f))
            }
            binding.cardImage.isVisible = true
            isPostValid[1] = true
            validateButton()
        }
    }

    private fun validateButton() {
        val isValid = isPostValid.filter { it }.size == 2
        binding.btnPost.apply {
            isEnabled = isValid
            setCardBackgroundColor(
                ContextCompat.getColor(
                    context, if (isValid) R.color.primary else R.color.gray
                )
            )
        }
    }

    private fun postNewStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            viewModel.postNewStory(
                currentToken,
                imageMultipart,
                binding.edtDesc.text.toString()
            ) { isPosted, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (isPosted) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

}