package id.derysudrajat.storyapp.ui.detail

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.RoundedCornersTransformation
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ActivityDetailBinding
import id.derysudrajat.storyapp.utils.TimeUtils.simpleTime
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.getParcelable<Story>(EXTRA_STORY)?.let {
            with(binding) {
                ivPhotos.load(it.photoUrl) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(8f))
                }
                tvUserName.text = it.name
                tvDesc.text = it.description
                tvDate.text = it.createdAt.simpleTime

                val subtitle = if (it.lat != 0.0) {
                    val geocoder = Geocoder(this@DetailActivity, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(it.lat, it.lon, 1)
                    if (addresses.isNotEmpty()) addresses.first().locality else null
                } else null

                toolbar.apply {
                    setToolbar(it.name, subtitle, View.TEXT_ALIGNMENT_TEXT_START)
                    setBack(this@DetailActivity) { onBackPressed() }
                }
            }
        }

    }
}