package com.channel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Colors.primary,
            secondary = Colors.secondary,
            background = Colors.background,
            error = Colors.error
        ),
        typography = AppTypography,
        content = content
    )
}
