package id.derysudrajat.storyapp.repo.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.derysudrajat.storyapp.data.model.Story


@Entity(tableName = "stories")
class StoryEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "desc")
    val description: String,
    @ColumnInfo(name = "photo")
    val photoUrl: String,

)

fun StoryEntity.toStory() = Story(this.photoUrl, "", this.name, this.description, 0.0, this.id, 0.0)

fun List<StoryEntity>.toStories(): MutableList<Story> {
    val listOfStory = mutableListOf<Story>()
    this.forEach { listOfStory.add(it.toStory()) }
    return listOfStory
}