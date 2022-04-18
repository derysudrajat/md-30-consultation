package id.derysudrajat.storyapp.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ItemStoryBinding

class StoryAdapter(
    private val context: Context,
    private val stories: List<Story>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                ivPhotos.load(story.photoUrl) { crossfade(true) }
                tvUserName.text = story.name
                tvDesc.text = story.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size
}