package id.derysudrajat.storyapp.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    val String.simpleTime
        get() : String {
            val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = apiFormat.parse(this.split("T").first())
            return DateFormat.format("EEEE, dd MMMM yyy", date).toString()
        }

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())
}