package com.channel.android.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.channel.android.R
import com.channel.android.ui.theme.Typography
import com.channel.data.model.profile.OnBoardingState
import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.utils.NetworkResult

@Composable
fun AuthLandingScreen(
    onboardingStatus: NetworkResult<OnboardingStatusResponse>,
    onNavigateToOnboarding: (OnBoardingState) -> Unit,
    onNavigateToMainApp: () -> Unit,
    onRefresh: () -> Unit
) {
    val refreshing = onboardingStatus is NetworkResult.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = refreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (onboardingStatus) {
                is NetworkResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is NetworkResult.Success -> {
                    LaunchedEffect(Unit) {
                        if (onboardingStatus.data.onboardingState == OnBoardingState.ON_BOARDING_COMPLETE) {
                            onNavigateToMainApp()
                        } else {
                            onNavigateToOnboarding(onboardingStatus.data.onboardingState)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    ErrorMessageDialog(onboardingStatus.errorMessage, onRefresh)
                }
                is NetworkResult.Exception -> {
                    ErrorMessageDialog(onboardingStatus.e.localizedMessage, onRefresh)
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ErrorMessageDialog(message: String?, onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(id = R.string.error_title), style = Typography.bodyLarge) },
        text = { Text(text = message ?: stringResource(id = R.string.error_unknown)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(id = R.string.error_ok_button))
            }
        }
    )
}

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
