package com.channel.android.ui.onboarding

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.channel.android.R
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.onboarding.viewmodel.OnboardingPictureViewModel
import com.channel.utils.MediaHelper
import com.channel.utils.PermissionHelper
import com.channel.utils.PermissionHelper.Companion.Permissions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingPictureFragment : Fragment() {

    @Inject
    lateinit var mediaHelper: MediaHelper

    @Inject
    lateinit var permissionHelper: PermissionHelper
    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingPictureViewModel by viewModels()
    private var imageUri: Uri? = null
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.updateSelectedImage(uri)
            }
        }
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    viewModel.updateSelectedImage(uri)
                }
            }
        }
    private val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchGallery()
            }
        }
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchCamera()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            OnboardingPictureScreen(
                uploadState = viewModel.uploadState.collectAsState().value,
                selectedImageUri = viewModel.selectedImageUri.collectAsState().value,
                onPickGallery = { requestStoragePermission() },
                onPickCamera = { requestCameraPermission() },
                onSubmit = {
                    viewModel.onSubmit()
                },
                onSuccess = {
                    findNavController().navigate(R.id.action_profileImage_to_profileBio)
                }
            )
        }
    }

    private fun requestStoragePermission() {
        if (permissionHelper.isPermissionGranted(Permissions.STORAGE)) {
            launchGallery()
        } else {
            storagePermissionLauncher.launch(Permissions.STORAGE)
        }
    }

    private fun launchGallery() {
        galleryLauncher.launch(mediaHelper.getImageMimeType())
    }

    private fun requestCameraPermission() {
        if (permissionHelper.isPermissionGranted(Permissions.CAMERA)) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Permissions.CAMERA)
        }
    }

    private fun launchCamera() {
        imageUri = mediaHelper.createImageUri()
        cameraLauncher.launch(imageUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
