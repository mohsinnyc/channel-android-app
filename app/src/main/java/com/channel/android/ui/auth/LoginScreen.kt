package com.channel.android.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.channel.android.R
import com.channel.android.ui.theme.ChannelTheme
import com.channel.android.ui.theme.Dimens
import com.channel.data.utils.NetworkResult

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    loginState: NetworkResult<*>,
    onLogin: (String, String) -> Unit,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(loginState) {
        when (loginState) {
            is NetworkResult.Success -> onLoginSuccess()
            is NetworkResult.Error -> errorMessage = loginState.message
            is NetworkResult.Exception -> errorMessage = loginState.errorMessage
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.auth_login_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(Dimens.paddingLarge))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.auth_username_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimens.paddingSmall))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.auth_password_label)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(Dimens.paddingSmall))

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
        }

        Button(
            onClick = { onLogin(username, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is NetworkResult.Loading
        ) {
            if (loginState is NetworkResult.Loading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(id = R.string.auth_login_button))
            }
        }

        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        TextButton(onClick = {
            onNavigateToSignUp()
        }) {
            Text(stringResource(id = R.string.auth_no_account))
        }
    }
}

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
