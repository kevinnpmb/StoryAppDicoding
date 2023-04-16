package com.kevin.storyappdicoding.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.core.R as RCore
import com.kevin.storyappdicoding.core.data.model.ApiResponse
import com.kevin.storyappdicoding.databinding.ActivityMapsBinding
import com.kevin.storyappdicoding.core.view.common.BaseActivity
import com.kevin.storyappdicoding.view.main.home.DetailBottomDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels()
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getMyLastLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initObserver()
        initListener()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initListener() {
        binding.apply {
            retryButton.setOnClickListener {
                viewModel.getStories()
            }
        }
    }

    private fun initObserver() {
        viewModel.storiesResult.observe(this) {
            binding.apply {
                retryButton.isVisible = it is ApiResponse.Error
                if (it is ApiResponse.Success) {
                    it.data?.listStory?.forEach { story ->
                        mMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    story.lat!!.toDouble(),
                                    story.lon!!.toDouble()
                                )
                            )
                                .title(story.name)
                        )?.apply {
                            tag = "MARKER_TAG:${story.id}"
                        }
                    }
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            setOnMarkerClickListener {
                DetailBottomDialogFragment.newInstance(it.tag.toString().substring(11))
                    .show(supportFragmentManager, "story_detail_tag")
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, 12.0f))
                true
            }
        }
        viewModel.getStories()
        setMapStyle()
        getMyLastLocation()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun locationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        mMap.isMyLocationEnabled = locationPermissionGranted()
        if (locationPermissionGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location == null) {
                    Toast.makeText(
                        this,
                        getString(RCore.string.your_location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
            }
        } else {
            requestLocationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}