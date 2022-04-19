package id.derysudrajat.storyapp.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.text.format.DateFormat
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.data.model.LoginResult
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object DataHelpers {

    const val authIcon =
        "https://www.kindpng.com/picc/m/269-2697881_computer-icons-user-clip-art-transparent-png-icon.png"
    val LoginResult.tokenBearer: String get() = "Bearer ${this.token}"

    val Context.isDarkMode get() = this.resources?.configuration?.uiMode?.and(UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES

    val String.simpleTime
        get() : String {
            val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = apiFormat.parse(this.split("T").first())
            return DateFormat.format("EEEE, dd MMMM yyy", date).toString()
        }

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

}