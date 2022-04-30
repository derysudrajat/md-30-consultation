package id.derysudrajat.storyapp.repo.remote.body

import com.google.gson.annotations.SerializedName

data class PostBody(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)
