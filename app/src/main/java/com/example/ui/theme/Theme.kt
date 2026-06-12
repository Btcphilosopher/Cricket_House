package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Brass,
    secondary = ForestGreen,
    tertiary = Burgundy,
    background = Navy,
    surface = Slate,
    onPrimary = NavyDark,
    onSecondary = Cream,
    onTertiary = Cream,
    onBackground = Cream,
    onSurface = Cream,
    surfaceVariant = NavyDark,
    onSurfaceVariant = CreamMuted
)

private val LightColorScheme = lightColorScheme(
    primary = Burgundy,
    secondary = ForestGreen,
    tertiary = Brass,
    background = Cream,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onTertiary = Navy,
    onBackground = Navy,
    onSurface = Navy,
    surfaceVariant = CreamMuted,
    onSurfaceVariant = Navy
)

@Composable
fun CricketHouseTheme(
    darkTheme: Boolean = true, // Force dark mode for that elegant, mission-control scoreboard feel!
    dynamicColor: Boolean = false, // Preserve our custom color identity instead of wall-matching
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
