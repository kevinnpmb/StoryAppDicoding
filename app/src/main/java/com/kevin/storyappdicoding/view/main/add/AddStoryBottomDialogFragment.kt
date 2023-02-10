package com.kevin.storyappdicoding.view.main.add

import android.Manifest.permission.CAMERA
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.databinding.FragmentAddBottomDialogBinding
import com.kevin.storyappdicoding.utils.Utilities.createCustomTempFile
import com.kevin.storyappdicoding.utils.Utilities.registerValidateIfEmpty
import com.kevin.storyappdicoding.utils.Utilities.uriToFile
import com.kevin.storyappdicoding.utils.Utilities.validate
import com.kevin.storyappdicoding.view.common.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddStoryBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBottomDialogBinding
    private val viewModel: AddStoryViewModel by viewModels()
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
        initObserver()
        initListener()
    }

    private fun initObserver() {
        viewModel.storyResult.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is ApiResponse.Loading -> {
                        baseActivity.loadingDialog.show()
                    }
                    is ApiResponse.Success -> {
                        baseActivity.loadingDialog.dismiss()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.add_story_success),
                            Toast.LENGTH_LONG
                        ).show()
                        dismiss()
                    }
                    is ApiResponse.Error -> {
                        baseActivity.loadingDialog.dismiss()
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnUpload.setOnClickListener {
                val validationList = mutableListOf<Boolean>().apply {
                    add(
                        storyDescription.validate(
                            requireContext(),
                            getString(R.string.description)
                        )
                    )
                    add(viewModel.photoFile != null)
                }
                if (viewModel.photoFile == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_set_photo),
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                baseActivity.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(baseActivity.packageManager)

        createCustomTempFile(baseActivity.application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.kevin.storyappdicoding",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            viewModel.photoFile = myFile
            binding.storyImage.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            viewModel.photoFile = myFile
            binding.storyImage.setImageURI(selectedImg)
        }
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}