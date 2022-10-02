package com.muhammhassan.storyapp.view.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModel<DetailStoryViewModel>()

    private val cameraCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel.image?.let { file ->
                    val image = BitmapFactory.decodeFile(file.path)
                    binding.imgStory.loadImage(image)
                }
            }
        }

    private val galleryCallback = registerForActivityResult(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<StoriesResponseModel>(DATA)
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                title = if(data == null) getString(R.string.add_story) else getString(R.string.story_by, data.name)
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
                                if (allPermissionGranted()) {
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

                                        cameraCallback.launch(intent)
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
                                galleryCallback.launch(chooser)
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
            binding.apply{
                buttonContent.gone()
                edtStory.isFocusableInTouchMode = false
                edtStory.isFocusable = false
            }

        }

        viewModel.data.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showLoading(true)
                }
                Status.SUCCESS -> {
                    showLoading(false)
                    if(viewModel.type == 1) {
                        binding.apply {
                            it.data?.let { data -> showData(data) }
                        }
                    }else{
                        finish()
                        setResult(RESULT_OK)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (!allPermissionGranted()) {
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

                    cameraCallback.launch(intent)
                }
            }
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

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val DATA = "data"
        private const val CAMERA_REQUEST_CODE = 1
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }
}