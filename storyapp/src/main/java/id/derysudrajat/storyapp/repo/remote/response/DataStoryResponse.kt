package id.derysudrajat.storyapp.repo.remote.response

import com.google.gson.annotations.SerializedName
import id.derysudrajat.storyapp.data.model.Story

data class DataStoryResponse(

	@field:SerializedName("listStory")
	val story: List<StoryResponse>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class StoryResponse(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)

fun StoryResponse.toStory() = Story(
	this.photoUrl ?: "",
	this.createdAt ?: "",
	this.name ?: "",
	this.description ?: "",
	this.lon ?: 0.0,
	this.id ?: "",
	this.lat ?: 0.0,
)

fun List<StoryResponse>.toStories(): MutableList<Story> {
	val stories = mutableListOf<Story>()
	this.forEach { stories.add(it.toStory()) }
	return stories
}
