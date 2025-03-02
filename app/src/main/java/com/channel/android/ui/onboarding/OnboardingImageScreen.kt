package com.channel.android.ui.onboarding

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.channel.data.utils.NetworkResult
import com.channel.ui.R
import com.channel.ui.component.MediaPicker
import com.channel.ui.theme.AppTypography
import com.channel.ui.theme.Colors
import com.channel.ui.theme.Dimens
import com.channel.utils.UriUtils
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun OnboardingImageScreen(
    uploadState: NetworkResult<Unit>,
    selectedImageUri: Uri?,
    onPickGallery: () -> Unit,
    onPickCamera: () -> Unit,
    onSubmit: (File) -> Unit
) {
    var isMediaPickerVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingStepIndicator(
                modifier = Modifier.padding(bottom = Dimens.spacingMedium)
            )

            OnboardingTitle()
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            OnboardingSubtext()
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            ProfileImageHolder(
                selectedImageUri = selectedImageUri,
                onPickMedia = { isMediaPickerVisible = true }
            )

            Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

            UploadStateHandler(uploadState)

            SubmitButton(uploadState = uploadState) {
                selectedImageUri?.let { uri ->
                    coroutineScope.launch {
                        val imageFile = UriUtils.getFileFromUri(context, uri)
                        if (imageFile != null && imageFile.exists()) {
                            onSubmit(imageFile)
                        }
                    }
                }
            }
        }
    }

    if (isMediaPickerVisible) {
        MediaPickerDialog(
            onPickGallery = {
                onPickGallery()
                isMediaPickerVisible = false
            },
            onPickCamera = {
                onPickCamera()
                isMediaPickerVisible = false
            },
            onDismiss = { isMediaPickerVisible = false }
        )
    }
}

@Composable
fun OnboardingStepIndicator(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.onboarding_step, 1, 3),
        style = AppTypography.bodyMedium,
        color = Colors.textSecondary,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun OnboardingTitle() {
    Text(
        text = stringResource(id = R.string.profile_image_title),
        style = AppTypography.titleLarge,
        color = Colors.textPrimary,
        textAlign = TextAlign.Center
    )
}

@Composable
fun OnboardingSubtext() {
    Text(
        text = stringResource(id = R.string.profile_image_subtext),
        style = AppTypography.bodyMedium,
        color = Colors.textSecondary,
        textAlign = TextAlign.Center
    )
}

@Composable
fun UploadStateHandler(uploadState: NetworkResult<Unit>) {
    when (uploadState) {
        is NetworkResult.Loading -> {
            Text(
                text = stringResource(id = R.string.profile_image_uploading),
                style = AppTypography.bodyMedium,
                color = Colors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
        is NetworkResult.Success -> {
            Text(
                text = stringResource(id = R.string.profile_image_upload_success),
                style = AppTypography.bodyMedium,
                color = Colors.textPrimary,
                textAlign = TextAlign.Center
            )
        }
        is NetworkResult.Error -> {
            Text(
                text = stringResource(id = R.string.profile_image_upload_failed, uploadState.errorMessage ?: "Unknown error"),
                style = AppTypography.bodyMedium,
                color = Colors.error,
                textAlign = TextAlign.Center
            )
        }
        is NetworkResult.Exception -> {
            Text(
                text = stringResource(id = R.string.profile_image_upload_failed, uploadState.errorMessage ?: "Unknown error"),
                style = AppTypography.bodyMedium,
                color = Colors.error,
                textAlign = TextAlign.Center
            )
        }
        else -> {} // No UI change for idle state
    }
}

@Composable
fun SubmitButton(uploadState: NetworkResult<Unit>, onSubmit: () -> Unit) {
    Button(
        onClick = onSubmit,
        enabled = uploadState !is NetworkResult.Loading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.paddingMedium)
    ) {
        if (uploadState is NetworkResult.Loading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        } else {
            Text(text = stringResource(id = R.string.profile_image_submit))
        }
    }
}

@Composable
fun ProfileImageHolder(
    selectedImageUri: Uri?,
    onPickMedia: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(Dimens.profileImageSize)
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.profileImageSize)
                .clip(CircleShape)
                .background(Colors.textSecondary),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = stringResource(id = R.string.profile_image_description),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.profile_image_placeholder),
                    modifier = Modifier.size(Dimens.iconLarge),
                    tint = Colors.background
                )
            }
        }

        IconButton(
            onClick = onPickMedia,
            modifier = Modifier
                .size(Dimens.iconExtraLarge)
                .align(Alignment.BottomCenter)
                .offset(y = Dimens.iconExtraLarge / 2)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = stringResource(id = R.string.profile_image_camera),
                tint = Colors.primary
            )
        }
    }
}

@Composable
fun MediaPickerDialog(
    onPickGallery: () -> Unit,
    onPickCamera: () -> Unit,
    onDismiss: () -> Unit
) {
    MediaPicker(
        onPickGallery = onPickGallery,
        onPickCamera = onPickCamera,
        onDismiss = onDismiss
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileImageScreen() {
    OnboardingImageScreen(
        uploadState = NetworkResult.Idle,
        selectedImageUri = null,
        onPickGallery = {},
        onPickCamera = {},
        onSubmit = {}
    )
}
