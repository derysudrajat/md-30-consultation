package id.derysudrajat.storyapp.data.model

import android.os.Parcelable
import id.derysudrajat.storyapp.repo.local.entity.StoryEntity
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

fun Story.toEntity() = StoryEntity(this.id,this.name, this.description,this.photoUrl)
fun List<Story>.toEntities(): MutableList<StoryEntity> {
    val lisOfEntity = mutableListOf<StoryEntity>()
    this.forEach { lisOfEntity.add(it.toEntity()) }
    return lisOfEntity
}