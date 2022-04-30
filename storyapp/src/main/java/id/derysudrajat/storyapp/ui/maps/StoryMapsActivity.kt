package id.derysudrajat.storyapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.databinding.ActivityStoryMapsBinding

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var markerClickListener: GoogleMap.OnMarkerClickListener
    private var currentStories = mutableListOf<Story>()
    private var currentMarker = mutableListOf<Marker>()

    companion object {
        const val EXTRA_STORIES = "extra_stories"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment =
            supportFragmentManager.findFragmentById(binding.mapFragment.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.toolbar.apply {
            setToolbar(
                "Map Stories", titleAlignment = View.TEXT_ALIGNMENT_TEXT_START
            )
            setBack(this@StoryMapsActivity) { onBackPressed() }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyLastLocation()
        intent.extras?.getParcelableArrayList<Story>(EXTRA_STORIES)?.let {
            currentStories = it.take(20).toMutableList()
            binding.rvStories.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = MapStoryAdapter(this@StoryMapsActivity, it.take(20), ::onStoryClicked)
            }
            showStoriesLocation(it.take(20))
        }
    }

    private fun onStoryClicked(position: Int) {
        markerClickListener.onMarkerClick(currentMarker[position])
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker[position].position, 17f))
    }

    private fun scrollListToPositionByName(name: String) {
        val story = currentStories.find { it.name == name }
        val position = currentStories.indexOf(story)

        binding.rvStories.apply {
            post { smoothScrollToPosition(position) }
        }
    }

    private fun showStoriesLocation(stories: List<Story>) {
        stories.forEach {
            val coordinate = LatLng(it.lat, it.lon)
            val option = MarkerOptions().position(coordinate).title(it.name)
            val marker = mMap.addMarker(option)
            marker?.let { mMarker -> currentMarker.add(mMarker) }
            markerClickListener = GoogleMap.OnMarkerClickListener { m ->
                scrollListToPositionByName(m.title ?: "")
                false
            }
            mMap.setOnMarkerClickListener(markerClickListener)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    //showStartMarker(location)
                    populateLocation(location)
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun populateLocation(location: Location) {
        val coordinate = LatLng(location.latitude, location.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 4f))
    }
}