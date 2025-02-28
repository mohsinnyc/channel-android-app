package com.channel.android.ui.onboarding

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.channel.data.utils.NetworkResult
import com.channel.ui.R
import com.channel.ui.theme.Colors
import com.channel.ui.theme.Dimens

@Composable
fun ProfileImageScreen(
    uploadState: NetworkResult<Unit>,
    selectedImageUri: Uri?,
    onSelectImage: () -> Unit,
    onTakePhoto: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingHeader()

        ProfileImagePlaceholder(selectedImageUri, onSelectImage)

        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        MediaPickerButton(onSelectImage)

        Spacer(modifier = Modifier.height(Dimens.paddingLarge))

        SubmitButton(uploadState, selectedImageUri, onSubmit)

        UploadStateMessage(uploadState)
    }
}

@Composable
fun OnboardingHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.onboarding_step, 1, 3),
            style = MaterialTheme.typography.bodyMedium,
            color = Colors.textSecondary
        )
        Spacer(modifier = Modifier.height(Dimens.spacingSmall))
        Text(
            text = stringResource(id = R.string.profile_image_title),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(Dimens.spacingExtraSmall))
        Text(
            text = stringResource(id = R.string.profile_image_subtext),
            style = MaterialTheme.typography.bodySmall,
            color = Colors.textSecondary
        )
    }
}

@Composable
fun ProfileImagePlaceholder(selectedImageUri: Uri?, onSelectImage: () -> Unit) {
    Box(
        modifier = Modifier
            .size(Dimens.profileImageSize)
            .clip(CircleShape)
            .background(Colors.textSecondary)
            .clickable { onSelectImage() },
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
                modifier = Modifier.size(Dimens.iconExtraLarge),
                tint = Colors.background
            )
        }
    }
}

@Composable
fun MediaPickerButton(onSelectImage: () -> Unit) {
    IconButton(
        onClick = onSelectImage,
        modifier = Modifier
            .size(Dimens.iconExtraLarge)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = stringResource(id = R.string.media_picker_camera),
            tint = Colors.background
        )
    }
}

@Composable
fun SubmitButton(uploadState: NetworkResult<Unit>, selectedImageUri: Uri?, onSubmit: () -> Unit) {
    Button(
        onClick = onSubmit,
        enabled = selectedImageUri != null && uploadState !is NetworkResult.Loading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingMedium)
    ) {
        Text(
            text = if (uploadState is NetworkResult.Loading)
                stringResource(id = R.string.profile_image_uploading)
            else stringResource(id = R.string.profile_image_submit)
        )
    }
}

@Composable
fun UploadStateMessage(uploadState: NetworkResult<Unit>) {
    when (uploadState) {
        is NetworkResult.Error -> {
            Text(
                text = stringResource(id = R.string.profile_image_upload_failed, uploadState.errorMessage ?: ""),
                color = Colors.error
            )
        }
        is NetworkResult.Success -> {
            Text(stringResource(id = R.string.profile_image_upload_success), color = Colors.primary)
        }
        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileImageScreen() {
    MaterialTheme {
        ProfileImageScreen(
            uploadState = NetworkResult.Idle,
            selectedImageUri = null,
            onSelectImage = {},
            onTakePhoto = {},
            onSubmit = {}
        )
    }
}
