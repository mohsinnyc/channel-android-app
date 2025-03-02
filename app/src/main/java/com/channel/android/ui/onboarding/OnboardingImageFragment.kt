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
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.onboarding.viewmodel.OnboardingImageViewModel
import com.channel.utils.MediaHelper
import com.channel.utils.PermissionHelper
import com.channel.utils.PermissionHelper.Permissions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingImageFragment : Fragment() {

    @Inject
    lateinit var mediaHelper: MediaHelper
    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingImageViewModel by viewModels()
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
            OnboardingImageScreen(
                uploadState = viewModel.uploadState.collectAsState().value,
                selectedImageUri = viewModel.selectedImageUri.collectAsState().value,
                onPickGallery = { requestStoragePermission() },
                onPickCamera = { requestCameraPermission() },
                onSubmit = { file -> viewModel.uploadProfileImage(file) }
            )
        }
    }

    private fun requestStoragePermission() {
        if (PermissionHelper.isPermissionGranted(requireContext(), Permissions.STORAGE)) {
            launchGallery()
        } else {
            storagePermissionLauncher.launch(Permissions.STORAGE)
        }
    }

    private fun launchGallery() {
        galleryLauncher.launch(mediaHelper.getImageMimeType())
    }

    private fun requestCameraPermission() {
        if (PermissionHelper.isPermissionGranted(requireContext(), Permissions.CAMERA)) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Permissions.CAMERA)
        }
    }

    private fun launchCamera() {
        imageUri = mediaHelper.createImageUri(requireContext())
        cameraLauncher.launch(imageUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
