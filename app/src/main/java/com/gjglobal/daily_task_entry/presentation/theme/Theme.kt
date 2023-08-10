package com.gjglobal.daily_task_entry.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gjglobal.hms_gj.presentation.theme.Typography


@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = ColorPrimary,
    secondary = Color.White,
    surface = Color.White,
    background = Color.White,
    onPrimary = Color.White,
    onSecondary = TextColor,
    onBackground = ButtonColor,
    onSurface = ButtonColor
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = ColorPrimary,
    secondary = Color.White,
    surface = Color.White,
    background = Color.White,
    onPrimary = Color.White,
    onSecondary = TextColor,
    onBackground = ButtonColor,
    onSurface = ButtonColor
)

@Composable
fun DailyActivityApplicationTheme(darkTheme: Boolean = true, content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}