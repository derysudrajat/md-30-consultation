package id.derysudrajat.storyapp.ui.base

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ActivityMainBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.utils.DataHelpers.tokenBearer
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var localStore: LocalStore
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            localStore.getUserLoginResult().collect {
                viewModel.getAllStories(it.tokenBearer)
                binding.toolbar.setToolbar(it.name, View.TEXT_ALIGNMENT_TEXT_START, true)

            }
        }

        viewModel.stories.observe(this, this::populateStories)
    }

    private fun populateStories(stories: List<Story>) {
        binding.rvStories.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = StoryAdapter(this@MainActivity, stories)
        }
    }
}