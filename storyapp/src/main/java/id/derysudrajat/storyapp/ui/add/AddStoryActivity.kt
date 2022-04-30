package id.derysudrajat.storyapp.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.component.LoadingView
import id.derysudrajat.storyapp.databinding.ActivityAddStoryBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.ui.camera.CameraActivity
import id.derysudrajat.storyapp.ui.maps.MapsFragment
import id.derysudrajat.storyapp.ui.maps.SearchMapsActivity
import id.derysudrajat.storyapp.utils.CameraUtils.reduceFileImage
import id.derysudrajat.storyapp.utils.CameraUtils.rotateBitmap
import id.derysudrajat.storyapp.utils.CameraUtils.uriToFile
import id.derysudrajat.storyapp.utils.ViewUtils
import id.derysudrajat.storyapp.utils.ViewUtils.tokenBearer
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
    private var getIsBackCamera: Boolean = false
    private val isPostValid = mutableListOf(false, false, false)
    private lateinit var loadingView: LoadingView
    private var isPickFromCamera = -1
    private lateinit var currentLocation : LatLng

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingView = LoadingView.create(this)

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

        binding.ivAvatar.load(ViewUtils.authIcon) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.edtDesc.doAfterTextChanged {
            isPostValid[0] = it.toString().isNotBlank()
            validateButton()
        }

        binding.btnPickImage.setOnClickListener { startGallery() }
        binding.btnTakeCamera.setOnClickListener { startCameraX() }
        binding.btnPickMaps.setOnClickListener { startSearchMap() }
        binding.btnPost.setOnClickListener { postNewStory() }
        viewModel.isLoading.observe(this, ::populateLoading)
    }

    private fun populateLoading(isLoading: Boolean) {
        if (isLoading) loadingView.showLoading(getString(R.string.create_story)) else loadingView.dismissLoading()
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

    private fun startSearchMap() {
        launcherMaps.launch(Intent(this, SearchMapsActivity::class.java))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            getIsBackCamera = isBackCamera
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.ivPost.load(result) {
                transformations(RoundedCornersTransformation(8f))
            }
            binding.cardImage.isVisible = true
            isPostValid[1] = true
            isPickFromCamera = 1
            validateButton()
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
            isPickFromCamera = 0
            validateButton()
        }
    }

    private val launcherMaps =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<LatLng>("location")?.let {
                    populateLocation(it)
                }
            }
        }

    private fun populateLocation(it: LatLng) {
        currentLocation = it
        supportFragmentManager.beginTransaction()
            .replace(binding.mapContainer.id, MapsFragment.newInstance(it)).commit()
        binding.contentMaps.isVisible = true
        isPostValid[2] = true
        validateButton()
    }

    private fun validateButton() {
        val isValid = isPostValid.filter { it }.size == isPostValid.size
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
            val file = reduceFileImage(getFile as File, getIsBackCamera, isPickFromCamera)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            viewModel.postNewStory(
                currentToken,
                imageMultipart,
                binding.edtDesc.text.toString(),
                currentLocation
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