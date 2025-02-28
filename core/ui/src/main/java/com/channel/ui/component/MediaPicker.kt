package com.channel.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
                style = AppTypography.titleLarge
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onPickCamera) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = stringResource(id = R.string.media_picker_camera),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onPickGallery) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gallery),
                        contentDescription = stringResource(id = R.string.media_picker_gallery),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimens.paddingLarge))
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.media_picker_cancel))
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