package id.derysudrajat.storyapp.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ItemStoryBinding
import id.derysudrajat.storyapp.ui.detail.DetailActivity

class StoryAdapter(
    private val context: Context,
    private val stories: List<Story>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: Story) {
            with(binding) {
                ivPhotos.load(story.photoUrl) { crossfade(true) }
                tvUserName.text = story.name
                tvDesc.text = story.description
                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            androidx.core.util.Pair(ivPhotos, "photo"),
                            androidx.core.util.Pair(tvUserName,"name"),
                            androidx.core.util.Pair(tvDesc, "desc"),
                        )
                    itemView.context.startActivity(
                        Intent(context, DetailActivity::class.java).apply { putExtra(DetailActivity.EXTRA_STORY, story) },
                        optionsCompat.toBundle()
                    )

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, stories[position])
    }

    override fun getItemCount(): Int = stories.size
}