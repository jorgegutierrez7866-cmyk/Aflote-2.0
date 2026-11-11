package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = OceanCyan,
    onPrimary = Color.White,
    primaryContainer = OceanFoam,
    onPrimaryContainer = OceanDeep,
    secondary = OceanTeal,
    onSecondary = Color.White,
    tertiary = AccentAmber,
    background = OceanLightBg,
    onBackground = TextPrimaryDark,
    surface = Color.White,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFFE2F0F8),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFFC0DCEB)
)

private val DarkColorScheme = darkColorScheme(
    primary = OceanGlow,
    onPrimary = OceanDeep,
    primaryContainer = OceanSurface,
    onPrimaryContainer = Color.White,
    secondary = OceanTeal,
    onSecondary = OceanDeep,
    tertiary = AccentGold,
    background = OceanDeep,
    onBackground = Color.White,
    surface = OceanAbyss,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF0F3657),
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0x3338E1E5)
)

@Composable
fun AfloteTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
