package id.derysudrajat.storyapp.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import id.derysudrajat.storyapp.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {

    private var currentLocation = LatLng(-34.0, 151.0)
    private lateinit var mMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        with(googleMap) {
            googleMap.addMarker(MarkerOptions().position(currentLocation))
            moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
    }

    private lateinit var binding: FragmentMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { currentLocation = it.getParcelable(ARG_LAT_LANG) ?: LatLng(-34.0, 151.0) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(binding.root.id) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        private const val ARG_LAT_LANG = "arg_lat_lang"
        fun newInstance(latLng: LatLng) = MapsFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_LAT_LANG, latLng) }
        }
    }
}