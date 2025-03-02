package com.channel.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.channel.ui.R
import com.channel.ui.theme.AppTypography
import com.channel.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPicker(
    onPickGallery: () -> Unit,
    onPickCamera: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.media_picker_title),
                style = AppTypography.titleLarge,
                modifier = Modifier.padding(bottom = Dimens.spacingMedium)
            )

            Button(
                onClick = {
                    onPickCamera()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.spacingSmall)
            ) {
                Text(text = stringResource(id = R.string.media_picker_camera))
            }

            Button(
                onClick = {
                    onPickGallery()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.spacingSmall)
            ) {
                Text(text = stringResource(id = R.string.media_picker_gallery))
            }

            Spacer(modifier = Modifier.height(Dimens.paddingLarge))

            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.media_picker_cancel))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMediaPicker() {
    Box(modifier = Modifier.fillMaxSize()) {
        MediaPicker(
            onPickGallery = {},
            onPickCamera = {},
            onDismiss = {}
        )
    }
}
