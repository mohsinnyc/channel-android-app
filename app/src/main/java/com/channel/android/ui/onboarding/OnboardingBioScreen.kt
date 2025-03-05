package com.channel.android.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.channel.android.R
import com.channel.ui.theme.AppTypography
import com.channel.ui.theme.Colors
import com.channel.ui.theme.Dimens
import com.channel.data.utils.NetworkResult

@Composable
fun OnboardingBioScreen(
    uploadState: NetworkResult<Unit>,
    bioText: String,
    onBioChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSkip: () -> Unit,
    onSuccess: () -> Unit
) {
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
            OnboardingStepIndicator(step = 2)
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            OnboardingTitle()
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            OnboardingSubtext()
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            BioInputField(bioText, onBioChange)
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            CharacterCounter(bioText.length)
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))

            UploadStateHandler(uploadState, onSuccess)
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))

            SaveButton(uploadState, bioText.isNotEmpty(), onSubmit)
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            SkipText(onSkip)
        }
    }
}

@Composable
private fun OnboardingStepIndicator(step: Int) {
    Text(
        text = stringResource(id = R.string.onboarding_step, step, 3),
        style = AppTypography.bodyMedium,
        color = Colors.textSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun OnboardingTitle() {
    Text(
        text = stringResource(id = R.string.profile_bio_title),
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
private fun BioInputField(bioText: String, onBioChange: (String) -> Unit) {
    OutlinedTextField(
        value = bioText,
        onValueChange = {
            if (it.length <= 150) onBioChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.paddingMedium)
            .height(Dimens.bioTextFieldHeight),
        textStyle = AppTypography.bodyMedium,
        label = { Text(stringResource(id = R.string.profile_bio_hint)) },
        minLines = 4,
        maxLines = 6,
        visualTransformation = VisualTransformation.None
    )
}

private @Composable
fun CharacterCounter(currentLength: Int) {
    Text(
        text = stringResource(id = R.string.bio_character_count, currentLength, 150),
        style = AppTypography.bodyMedium,
        color = Colors.textSecondary,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun UploadStateHandler(uploadState: NetworkResult<Unit>, onSuccess: () -> Unit) {
    when (uploadState) {
        is NetworkResult.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        is NetworkResult.Success -> {
            Text(
                text = stringResource(id = R.string.profile_bio_upload_success),
                style = AppTypography.bodyMedium,
                color = Colors.textPrimary,
                textAlign = TextAlign.Center
            )
            onSuccess()
        }
        is NetworkResult.Error, is NetworkResult.Exception -> {
            val errorMessage = when (uploadState) {
                is NetworkResult.Error -> uploadState.errorMessage ?: "Unknown error"
                is NetworkResult.Exception -> uploadState.errorMessage ?: "Unknown error"
                else -> "Unknown error"
            }
            Text(
                text = stringResource(id = R.string.profile_bio_upload_failed, errorMessage),
                style = AppTypography.bodyMedium,
                color = Colors.error,
                textAlign = TextAlign.Center
            )
        }
        else -> {}
    }
}

@Composable
private fun SaveButton(
    uploadState: NetworkResult<Unit>,
    isEnabled: Boolean,
    onSubmit: () -> Unit
) {
    Button(
        onClick = onSubmit,
        enabled = isEnabled && uploadState !is NetworkResult.Loading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.paddingMedium)
    ) {
        Text(text = stringResource(id = R.string.profile_bio_submit))
    }
}

@Composable
private fun SkipText(onSkip: () -> Unit) {
    Text(
        text = stringResource(id = R.string.profile_bio_skip),
        color = Colors.textSecondary,
        style = AppTypography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(Dimens.spacingSmall)
            .clickable { onSkip() }
    )
}

@Preview
@Composable
fun PreviewOnboardingBioScreen() {
    OnboardingBioScreen(
        uploadState = NetworkResult.Idle,
        bioText = "",
        onBioChange = {},
        onSubmit = {},
        onSkip = {},
        onSuccess = {}
    )
}
