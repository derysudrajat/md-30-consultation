package id.derysudrajat.storyapp.ui.maps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ItemStoryMapBinding
import id.derysudrajat.storyapp.ui.detail.DetailActivity

class MapStoryAdapter(
    private val context: Context,
    private val stories: List<Story>,
    private val onItemClicked : (position: Int) -> Unit
) : RecyclerView.Adapter<MapStoryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemStoryMapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: Story, onItemClicked: (Int) -> Unit, position: Int) {
            with(binding) {
                ivPhotos.load(story.photoUrl) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(16f))
                }
                tvUserName.text = story.name
                tvDesc.text = story.description
                btnToDetail.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            androidx.core.util.Pair(ivPhotos, "photo"),
                            androidx.core.util.Pair(tvUserName, "name"),
                            androidx.core.util.Pair(tvDesc, "desc"),
                        )
                    itemView.context.startActivity(
                        Intent(
                            context,
                            DetailActivity::class.java
                        ).apply { putExtra(DetailActivity.EXTRA_STORY, story) },
                        optionsCompat.toBundle()
                    )
                }
                root.setOnClickListener { onItemClicked(position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoryMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, stories[position], onItemClicked, position)
    }

    override fun getItemCount(): Int = stories.size
}