package com.channel.android.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.channel.android.ui.auth.LoginScreen
import com.channel.android.ui.theme.ChannelTheme
import com.channel.data.utils.NetworkResult

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ChannelTheme {
        LoginScreen(
            onNavigateToSignUp = {},
            loginState = NetworkResult.Idle,
            onLogin = { _, _ -> },
            onLoginSuccess = {}
        )
    }
}