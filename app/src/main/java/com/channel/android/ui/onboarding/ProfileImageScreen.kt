package com.channel.android.ui.onboarding

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
import com.channel.android.R
import com.channel.data.utils.NetworkResult
import com.channel.ui.R.*
import com.channel.ui.theme.Dimens
import com.channel.ui.theme.Colors

@Composable
fun ProfileImageScreen(
    uploadState: NetworkResult<Unit>,
    selectedImageUri: String?,
    onSelectImage: () -> Unit,
    onTakePhoto: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    painter = painterResource(id = drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.profile_image_placeholder),
                    modifier = Modifier.size(Dimens.placeholderIconSize),
                    tint = Colors.background
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)) {
            IconButton(onClick = onTakePhoto) {
                Icon(
                    painter = painterResource(id = drawable.ic_camera),
                    contentDescription = "Camera Icon"
                )
            }
            IconButton(onClick = onSelectImage) {
                Icon(
                    painter = painterResource(id = drawable.ic_gallery),
                    contentDescription = "Gallery Icon"
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.paddingLarge))

        Button(
            onClick = onSubmit,
            enabled = selectedImageUri != null && uploadState !is NetworkResult.Loading
        ) {
            Text(
                text = if (uploadState is NetworkResult.Loading)
                    stringResource(id = R.string.profile_image_uploading)
                else stringResource(id = R.string.profile_image_submit)
            )
        }

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
