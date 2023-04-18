package com.kevin.storyappdicoding.view.main.add

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.data.model.Response
import com.kevin.storyappdicoding.databinding.FragmentAddBottomDialogBinding
import com.kevin.storyappdicoding.utils.Utilities.registerValidateIfEmpty
import com.kevin.storyappdicoding.utils.Utilities.rotateBitmap
import com.kevin.storyappdicoding.utils.Utilities.serializable
import com.kevin.storyappdicoding.utils.Utilities.uriToFile
import com.kevin.storyappdicoding.utils.Utilities.validate
import com.kevin.storyappdicoding.view.camera.CameraActivity
import com.kevin.storyappdicoding.view.camera.CameraActivity.Companion.IS_BACK_CAMERA
import com.kevin.storyappdicoding.view.camera.CameraActivity.Companion.PICTURE
import com.kevin.storyappdicoding.view.common.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddStoryBottomDialogFragment(private val listener: AddStoryListener) :
    BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBottomDialogBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: AddStoryViewModel by viewModels()
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[ACCESS_FINE_LOCATION] != true && permissions[ACCESS_COARSE_LOCATION] != true) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                binding.showLocation.isChecked = false
            }
        }
    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            }
        }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.serializable<File>(PICTURE)
            val isBackCamera = it.data?.getBooleanExtra(IS_BACK_CAMERA, true) as Boolean
            viewModel.isBackCamera = isBackCamera
            viewModel.photoFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(viewModel.photoFile?.path),
                isBackCamera
            )
            binding.storyImage.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            viewModel.isBackCamera = null
            viewModel.photoFile = myFile
            binding.storyImage.setImageURI(selectedImg)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBottomDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.storyDescription.registerValidateIfEmpty(
            requireContext(),
            getString(R.string.description)
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initObserver()
        initListener()
    }

    private fun initObserver() {
        viewModel.apply {
            storyResult.observe(viewLifecycleOwner) {
                binding.apply {
                    when (it) {
                        is Response.Loading -> {
                            baseActivity.loadingDialog.show()
                        }
                        is Response.Success -> {
                            baseActivity.loadingDialog.dismiss()
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.add_story_success),
                                Toast.LENGTH_LONG
                            ).show()
                            dismiss()
                            listener.onStoryAddedSuccessfully()
                        }
                        is Response.Error -> {
                            baseActivity.loadingDialog.dismiss()
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }

            isShowLocation.observe(viewLifecycleOwner) {
                if (it) {
                    getMyLastLocation()
                }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            showLocation.setOnCheckedChangeListener { _, state ->
                viewModel.isShowLocation.value = state
            }
            btnUpload.setOnClickListener {
                val validationList = mutableListOf<Boolean>().apply {
                    add(
                        storyDescription.validate(
                            requireContext(),
                            getString(R.string.description)
                        )
                    )
                }
                if (viewModel.photoFile == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_set_photo),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (viewModel.location == null && viewModel.isShowLocation.value == true) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_location_registered),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (validationList.all { it }) {
                    viewModel.addStory(
                        storyDescription.editText?.text.toString()
                    )
                }
            }

            takePhoto.setOnClickListener {
                startTakePhoto()
            }

            gallery.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun locationPermissionGranted() = REQUIRED_LOCATION_PERMISSIONS.any {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun cameraPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        if (locationPermissionGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                viewModel.location = location
                if (location == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.your_location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestLocationPermissionLauncher.launch(
                REQUIRED_LOCATION_PERMISSIONS
            )
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    override fun onStart() {
        super.onStart()
        if (!cameraPermissionsGranted()) {
            if (shouldShowRequestPermissionRationale(CAMERA)) {
                showRationaleDialog {
                    dismiss()
                }
            } else {
                requestCameraPermissionLauncher.launch(CAMERA)
            }
        }
    }

    private fun showRationaleDialog(cancelCallback: (() -> Unit)? = null) {
        val builder =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.MaterialAlertDialog_Rounded
            )
                .setTitle(R.string.permission_denied)
                .setMessage(R.string.permission_denied_msg)
                .setIcon(R.drawable.stapp)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", baseActivity.packageName, null)
                    })
                    dialog.dismiss()
                    dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                    cancelCallback?.invoke()
                }

        builder.create().show()
    }

    interface AddStoryListener {
        fun onStoryAddedSuccessfully()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_LOCATION_PERMISSIONS =
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    }
}