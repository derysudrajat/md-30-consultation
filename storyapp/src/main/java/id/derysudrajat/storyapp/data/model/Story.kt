package id.derysudrajat.storyapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Double,
    val id: String,
    val lat: Double
) : Parcelable