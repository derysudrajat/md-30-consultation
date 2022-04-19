package id.derysudrajat.storyapp.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ActivityMainBinding
import id.derysudrajat.storyapp.repo.local.LocalStore
import id.derysudrajat.storyapp.ui.add.AddStoryActivity
import id.derysudrajat.storyapp.ui.settings.SettingsActivity
import id.derysudrajat.storyapp.utils.DataHelpers.isDarkMode
import id.derysudrajat.storyapp.utils.DataHelpers.tokenBearer
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var localStore: LocalStore
    private val viewModel: MainViewModel by viewModels()
    private var currentToken = ""

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            localStore.getUserLoginResult().collect {
                currentToken = it.tokenBearer
                viewModel.getAllStories(it.tokenBearer)
                binding.toolbar.setToolbar(it.name, null, View.TEXT_ALIGNMENT_TEXT_START, true)
                binding.toolbar.setMenu(R.menu.main_menu) { items ->
                    if (items.itemId == R.id.action_settings) startActivity(
                        Intent(this@MainActivity, SettingsActivity::class.java)
                    )
                }
            }
        }
        binding.fabAdd.setBackgroundColor(
            ContextCompat.getColor(this, if (isDarkMode) R.color.white else R.color.primary)
        )
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY < oldScrollY) binding.fabAdd.extend() else binding.fabAdd.shrink()
        })

        viewModel.stories.observe(this, this::populateStories)
        binding.fabAdd.setOnClickListener {
            startForResult.launch(Intent(this, AddStoryActivity::class.java))
        }
    }

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getAllStories(currentToken)
        }
    }


    private fun populateStories(stories: List<Story>) {
        binding.rvStories.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = StoryAdapter(this@MainActivity, stories)
        }
    }
}