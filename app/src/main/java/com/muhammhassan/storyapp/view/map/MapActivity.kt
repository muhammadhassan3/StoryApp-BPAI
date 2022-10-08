package com.muhammhassan.storyapp.view.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.databinding.ActivityMapBinding
import com.muhammhassan.storyapp.utils.api.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private var map: GoogleMap? = null

    private val viewModel by viewModel<MapViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.story_spread)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        observeViewModel()
        viewModel.getStoriesList()
    }

    private fun observeViewModel() {
        viewModel.data.observe(this) {
            when (it.status) {
                Status.LOADING -> viewModel.setStatus(getString(R.string.getting_data))
                Status.SUCCESS -> {
                    viewModel.setStatus(getString(R.string.showing_marker))
                    it.data?.let { it1 -> addMarkers(it1) }
                }
                Status.NO_DATA -> {
                    viewModel.setStatus(getString(R.string.no_data))
                }
                Status.ERROR -> {
                    viewModel.setStatus(getString(R.string.error, it.message))
                }
            }
        }

        viewModel.status.observe(this) {
            binding.tvStatus.text = it
        }
    }

    private fun addMarkers(data: List<StoriesResponseModel>) {
        for (item in data) {
            if (item.lat != null && item.long != null) {
                val location = LatLng(item.lat, item.long)
                val title = item.name

                val marker = MarkerOptions().also {
                    it.title(title)
                    it.position(location)
                    it.draggable(false)
                }

                map?.addMarker(marker)
            }
        }
        viewModel.setStatus(getString(R.string.all_markers_added))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
}