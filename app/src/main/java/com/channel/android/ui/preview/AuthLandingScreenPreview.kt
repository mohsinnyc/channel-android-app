package com.channel.android.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.channel.android.ui.auth.AuthLandingScreen
import com.channel.data.utils.NetworkResult

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingStatusScreen() {
    AuthLandingScreen(
        onboardingStatus = NetworkResult.Loading,
        onNavigateToOnboarding = {},
        onNavigateToMainApp = {},
        onRefresh = {}
    )
}