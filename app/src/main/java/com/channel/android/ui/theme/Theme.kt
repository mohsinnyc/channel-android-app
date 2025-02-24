package com.channel.android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),        // Vibrant purple accent
    secondary = Color(0xFF03DAC5),      // Soft teal secondary
    background = Color(0xFFFFFFFF),     // White background
    surface = Color(0xFFF5F5F5),        // Light gray surface
    onPrimary = Color.White,            // White text on primary
    onSecondary = Color.Black,          // Black text on secondary
    onBackground = Color.Black,         // Black text on background
    onSurface = Color.Black             // Black text on surface
)

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),        // Soft purple for dark mode
    secondary = Color(0xFF03DAC5),      // Same teal for consistency
    background = Color(0xFF121212),     // Dark gray background
    surface = Color(0xFF1F1F1F),        // Slightly lighter surface
    onPrimary = Color.Black,            // Black text on primary
    onSecondary = Color.Black,          // Black text on secondary
    onBackground = Color.White,         // White text on background
    onSurface = Color.White             // White text on surface
)

@Composable
fun ChannelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
