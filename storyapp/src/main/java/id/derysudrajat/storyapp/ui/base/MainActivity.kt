package id.derysudrajat.storyapp.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DefaultItemAnimator
import com.faltenreich.skeletonlayout.Skeleton
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ActivityMainBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.ui.add.AddStoryActivity
import id.derysudrajat.storyapp.ui.maps.StoryMapsActivity
import id.derysudrajat.storyapp.ui.settings.SettingsActivity
import id.derysudrajat.storyapp.utils.ViewUtils.buildSkeleton
import id.derysudrajat.storyapp.utils.ViewUtils.isDarkMode
import id.derysudrajat.storyapp.utils.ViewUtils.tokenBearer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var localStore: LocalStore
    private val viewModel: MainViewModel by viewModels()
    private var currentToken = ""
    private lateinit var skeleton: Skeleton
    private lateinit var storyAdapter: StoryAdapter
    private var isLoading = false
    private val currentStories = arrayListOf<Story>()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        skeleton = binding.rvStoriesLoading.buildSkeleton(this, R.layout.item_story)
        storyAdapter = StoryAdapter(this@MainActivity)
        binding.rvStories.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }

        lifecycleScope.launch {
            storyAdapter.loadStateFlow.collect{
                when(it.refresh){
                    is LoadState.Loading -> {
                        isLoading = true
                        viewModel.setLoading(true)
                    }
                    is LoadState.Error -> {}
                    is LoadState.NotLoading -> {
                        if (isLoading){
                            viewModel.setLoading(false)
                            isLoading = false
                        }
                        lifecycleScope.launch {
                            currentStories.clear()
                            currentStories.addAll(storyAdapter.snapshot().items)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            localStore.getUserLoginResult().collect {
                currentToken = it.tokenBearer
                viewModel.getAllStories(it.tokenBearer)
                binding.toolbar.setToolbar(it.name, null, View.TEXT_ALIGNMENT_TEXT_START, true)
                binding.toolbar.setMenu(R.menu.main_menu, ::onOptionMenuSelected)
            }
        }
        binding.fabAdd.setBackgroundColor(
            ContextCompat.getColor(this, if (isDarkMode) R.color.white else R.color.primary)
        )
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY < oldScrollY) binding.fabAdd.extend() else binding.fabAdd.shrink()
        })

        viewModel.stories.observe(this, this::populateStories)
        viewModel.isLoading.observe(this, this::populateLoading)
        binding.fabAdd.setOnClickListener {
            startForResult.launch(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun onOptionMenuSelected(items: MenuItem) {
        val target = when (items.itemId) {
            R.id.action_settings -> SettingsActivity::class.java
            R.id.action_maps -> StoryMapsActivity::class.java
            else -> null
        }
        target?.let { targetClass ->
            startActivity(Intent(this@MainActivity, targetClass).apply {
                if (items.itemId == R.id.action_maps) putExtra(
                    StoryMapsActivity.EXTRA_STORIES, currentStories
                )
            })
        }
    }

    private fun populateLoading(isLoading: Boolean) {
        if (isLoading) skeleton.showSkeleton() else {
            binding.rvStoriesLoading.visibility = View.GONE
            skeleton.showOriginal()
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getAllStories(currentToken)
            }
        }


    private fun populateStories(stories: PagingData<Story>) {
        storyAdapter.submitData(lifecycle, stories)
    }
}