package com.channel.android.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.channel.android.ui.theme.ChannelTheme
import com.channel.data.session.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    authState: StateFlow<AuthState>, // Observe authentication state
    onLogout: () -> Unit,
    onRefreshToken: () -> Unit
) {
    val currentAuthState by authState.collectAsState(initial = AuthState.LOGGED_OUT)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Auth State: $currentAuthState")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRefreshToken) {
            Text("Refresh Token")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
            Text("Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ChannelTheme {
        HomeScreen(
            authState = MutableStateFlow(AuthState.AUTHENTICATED),
            onLogout = {},
            onRefreshToken = {}
        )
    }
}
