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
import com.channel.data.utils.NetworkResult
import com.channel.ui.theme.Dimens

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    signUpState: NetworkResult<*>,
    onSignUp: (String, String, String) -> Unit,
    onSignUpSuccess: () -> Unit // Callback for when sign-up is successful
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is NetworkResult.Success -> onSignUpSuccess()
            is NetworkResult.Error -> errorMessage = signUpState.errorMessage
            is NetworkResult.Exception -> errorMessage = signUpState.errorMessage
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
        Text(text = stringResource(id = R.string.auth_signup_title), style = MaterialTheme.typography.headlineMedium)
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

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(id = R.string.auth_confirm_password_label)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(Dimens.paddingSmall))

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
        }

        Button(
            onClick = { onSignUp(username, password, confirmPassword) },
            modifier = Modifier.fillMaxWidth(),
            enabled = signUpState !is NetworkResult.Loading
        ) {
            if (signUpState is NetworkResult.Loading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(id = R.string.auth_signup_button))
            }
        }

        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        TextButton(onClick = {
            onNavigateToLogin()
        }) {
            Text(stringResource(id = R.string.auth_already_have_account))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    ChannelTheme {
        SignUpScreen(
            onNavigateToLogin = {},
            signUpState = NetworkResult.Idle,
            onSignUp = { _, _, _ -> },
            onSignUpSuccess = {}
        )
    }
}
