package com.tanya.currencyconverter.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen, // Or PrimaryBlue
    secondary = SecondaryTeal, // Or SecondaryGold
    tertiary = TertiaryOrange, // Or TertiaryRed
    background = DarkBackground,
    surface = DarkSurface,
    // ... add on colors for contrast
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen, // Or PrimaryBlue
    secondary = SecondaryTeal, // Or SecondaryGold
    tertiary = TertiaryOrange, // Or TertiaryRed
    background = LightBackground,
    surface = LightSurface,
    // ... add on colors for contrast
)

@Composable
fun CurrencyConverterTheme(
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