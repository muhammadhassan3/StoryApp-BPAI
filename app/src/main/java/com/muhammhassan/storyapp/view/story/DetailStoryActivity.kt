package com.muhammhassan.storyapp.view.story

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.databinding.ActivityDetailStoryBinding
import com.muhammhassan.storyapp.utils.Extension.gone
import com.muhammhassan.storyapp.utils.Extension.loadImage
import com.muhammhassan.storyapp.utils.Extension.show
import com.muhammhassan.storyapp.utils.Extension.showToast
import com.muhammhassan.storyapp.utils.Utils.createCustomTempFile
import com.muhammhassan.storyapp.utils.api.Status
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModel<DetailStoryViewModel>()
    private var fusedLocation: FusedLocationProviderClient? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val cameraIntentCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel.image?.let { file ->
                    val image = BitmapFactory.decodeFile(file.path)
                    binding.imgStory.loadImage(image)
                }
            }
        }

    private val galleryIntentCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImage = result.data?.data as Uri
            viewModel.setImage(selectedImage, this@DetailStoryActivity)
            viewModel.image?.let { file ->
                val image = BitmapFactory.decodeFile(file.path)
                binding.imgStory.loadImage(image)
            }
        }
    }

    private val locationIntentCallback =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    Log.e("DetailStoryActivity", "LocationIntentCallback: Location activated")
                }
                RESULT_CANCELED -> {
                    Log.e(
                        "DetailStoryActivity",
                        "LocationIntentCallback : Location request canceled"
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) intent.getParcelableExtra(
            DATA,
            StoriesResponseModel::class.java
        ) else intent.getParcelableExtra(DATA)
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                title = if (data == null) getString(R.string.add_story) else getString(
                    R.string.story_by,
                    data.name
                )
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }

            btnAddImage.setOnClickListener {
                val dialog = AlertDialog.Builder(this@DetailStoryActivity)
                    .setTitle(getString(R.string.choose_source))
                    .setItems(
                        arrayOf(getString(R.string.camera), getString(R.string.gallery))
                    ) { _, which ->
                        when (which) {
                            0 -> {
                                if (allPermissionGranted(REQUIRED_PERMISSION)) {
                                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    intent.resolveActivity(packageManager)

                                    createCustomTempFile(this@DetailStoryActivity).also {
                                        val photoUri = FileProvider.getUriForFile(
                                            this@DetailStoryActivity,
                                            "com.muhammhassan.storyapp",
                                            it
                                        )
                                        viewModel.setImage(it.absolutePath)
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                                        cameraIntentCallback.launch(intent)
                                    }
                                } else {
                                    ActivityCompat.requestPermissions(
                                        this@DetailStoryActivity,
                                        REQUIRED_PERMISSION,
                                        CAMERA_REQUEST_CODE
                                    )
                                }
                            }
                            1 -> {
                                val intent = Intent()
                                intent.action = Intent.ACTION_GET_CONTENT
                                intent.type = "image/*"
                                val chooser =
                                    Intent.createChooser(intent, getString(R.string.choose_image))
                                galleryIntentCallback.launch(chooser)
                            }
                        }
                    }

                dialog.show()
            }

            btnSave.setOnClickListener {
                val text = edtStory.text?.trim().toString()
                viewModel.setText(text)
                viewModel.save()
            }
        }

        if (data != null) {
            viewModel.setData(data)
            viewModel.type = 1
            binding.apply {
                buttonContent.gone()
                edtStory.isFocusableInTouchMode = false
                edtStory.isFocusable = false
            }
        } else {
            fusedLocation = LocationServices.getFusedLocationProviderClient(this)
            createLocationRequest()
            createLocationCallback()
            startLocationUpdates()
        }

        viewModel.data.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoading(true)
                }
                Status.SUCCESS -> {
                    showLoading(false)
                    if (viewModel.type == 1) {
                        binding.apply {
                            it.data?.let { data -> showData(data) }
                        }
                    } else {
                        setResult(RESULT_OK)
                        finish()
                        showToast(getString(R.string.add_story_success))
                    }
                }
                Status.NO_DATA -> {
                    showLoading(false)
                    showToast(getString(R.string.no_data))
                }
                Status.ERROR -> {
                    showLoading(false)
                    it.message?.let { message -> showToast(message) }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocation?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    private fun stopLocatonUpdates() {
        fusedLocation?.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)

        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getCurrentLocation()
            }
            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        locationIntentCallback.launch(
                            IntentSenderRequest.Builder(e.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        showToast(sendEx.message.toString())
                    }
                }
            }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (location in result.locations) {
                    Log.e(
                        "Location Result",
                        "onLocationResult: ${location.latitude}/${location.longitude}",
                    )
                    viewModel.setLocation(location)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (!allPermissionGranted(REQUIRED_PERMISSION)) {
                showToast(getString(R.string.permission_denied))
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.resolveActivity(packageManager)

                createCustomTempFile(this@DetailStoryActivity).also {
                    val photoUri = FileProvider.getUriForFile(
                        this@DetailStoryActivity,
                        "com.muhammhassan.storyapp",
                        it
                    )
                    viewModel.setImage(it.absolutePath)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                    cameraIntentCallback.launch(intent)
                }
            }
        } else if (requestCode == LOCATION_REQUEST_CODE) {
            if (!allPermissionGranted(REQUIRED_LOCATION_PERMISSION)) {
                showToast(getString(R.string.permission_denied))
            } else {
                getCurrentLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocatonUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocatonUpdates()
    }

    private fun getCurrentLocation() {
        if (allPermissionGranted(REQUIRED_LOCATION_PERMISSION)) {
            fusedLocation?.lastLocation?.addOnSuccessListener { location ->
                if (location != null) {
                    Log.e("DetailStoryActivity", "getCurrentLocation: Location settings granted")
                } else {
                    Log.e("DetailStoryActivity", "getCurrentLocation: Location settings granted")
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_LOCATION_PERMISSION,
                LOCATION_REQUEST_CODE
            )
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                mainContent.gone()
                pbar.show()
            } else {
                mainContent.show()
                pbar.gone()
            }
        }
    }

    private fun showData(data: StoriesResponseModel) {
        binding.apply {
            imgStory.loadImage(data.photoUrl)
            edtStory.setText(data.description)
        }
    }

    private fun allPermissionGranted(permission: Array<String>) = permission.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val DATA = "data"
        private const val CAMERA_REQUEST_CODE = 1
        private const val LOCATION_REQUEST_CODE = 2
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private val REQUIRED_LOCATION_PERMISSION = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}