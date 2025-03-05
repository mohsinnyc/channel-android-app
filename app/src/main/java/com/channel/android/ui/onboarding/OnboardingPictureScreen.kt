package com.channel.android.ui.onboarding

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.channel.android.R
import com.channel.data.utils.NetworkResult
import com.channel.ui.R.*
import com.channel.ui.component.MediaPicker
import com.channel.ui.theme.AppTypography
import com.channel.ui.theme.Colors
import com.channel.ui.theme.Dimens

@Composable
fun OnboardingPictureScreen(
    uploadState: NetworkResult<Unit>,
    selectedImageUri: Uri?,
    onPickGallery: () -> Unit,
    onPickCamera: () -> Unit,
    onSubmit: () -> Unit,
    onSuccess: () -> Unit
) {
    var isMediaPickerVisible by remember { mutableStateOf(false) }
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

            UploadStateHandler(uploadState, onSuccess)

            SaveButton(uploadState = uploadState) {
                onSubmit()
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
private fun OnboardingStepIndicator(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.onboarding_step, 1, 3),
        style = AppTypography.bodyMedium,
        color = Colors.textSecondary,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun OnboardingTitle() {
    Text(
        text = stringResource(id = R.string.profile_image_title),
        style = AppTypography.titleLarge,
        color = Colors.textPrimary,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun OnboardingSubtext() {
    Text(
        text = stringResource(id = R.string.profile_image_subtext),
        style = AppTypography.bodyMedium,
        color = Colors.textSecondary,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun UploadStateHandler(uploadState: NetworkResult<Unit>, onSuccess: () -> Unit) {
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
            onSuccess()
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
private fun SaveButton(uploadState: NetworkResult<Unit>, onSubmit: () -> Unit) {
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
            Text(text = stringResource(id = R.string.profile_image_save))
        }
    }
}

@Composable
private fun ProfileImageHolder(
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
                painter = painterResource(id = drawable.ic_camera),
                contentDescription = stringResource(id = R.string.profile_image_camera),
                tint = Colors.primary
            )
        }
    }
}

@Composable
private fun MediaPickerDialog(
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
private fun PreviewProfileImageScreen() {
    OnboardingPictureScreen(
        uploadState = NetworkResult.Idle,
        selectedImageUri = null,
        onPickGallery = {},
        onPickCamera = {},
        onSubmit = {},
        onSuccess = {}
    )
}
