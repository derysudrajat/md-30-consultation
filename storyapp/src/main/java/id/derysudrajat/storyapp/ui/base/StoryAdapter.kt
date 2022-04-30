package id.derysudrajat.storyapp.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ItemStoryBinding
import id.derysudrajat.storyapp.ui.detail.DetailActivity

class StoryAdapter(
    private val context: Context
) : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK)  {

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

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: data = ${getItem(position)}")
        getItem(position)?.let { holder.bind(context, it) }
    }
}