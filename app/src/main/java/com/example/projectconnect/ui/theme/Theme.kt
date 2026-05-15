package com.example.projectconnect.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = MidnightBackground,
    primaryContainer = Color(0xFF053447),
    onPrimaryContainer = Color(0xFFB8F3FF),
    secondary = MintAccent,
    onSecondary = MidnightBackground,
    secondaryContainer = Color(0xFF123A2A),
    onSecondaryContainer = Color(0xFFC7FFD9),
    tertiary = CoralAccent,
    onTertiary = MidnightBackground,
    background = MidnightBackground,
    onBackground = DarkTextPrimary,
    surface = MidnightSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = MidnightSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkOutline,
    error = DarkError,
    errorContainer = Color(0xFF4D1020),
    onError = MidnightBackground
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    secondary = MintAccent,
    tertiary = CoralAccent,
    background = MidnightBackground,
    surface = MidnightSurface,
    surfaceVariant = MidnightSurfaceVariant,
    onPrimary = MidnightBackground,
    onSecondary = MidnightBackground,
    onTertiary = MidnightBackground,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkOutline,
    error = DarkError,
    errorContainer = Color(0xFF4D1020),
    onError = MidnightBackground
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp)
)

@Composable
fun ProjectConnectTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
    ) {
        PunkBackground(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}

@Composable
private fun PunkBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    PunkInk,
                    MidnightBackground,
                    Color(0xFF1A0D28),
                    Color(0xFF101D24)
                ),
                start = Offset(0f, 0f),
                end = Offset(1200f, 1600f)
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PunkMagenta.copy(alpha = 0.28f),
                            Color.Transparent
                        ),
                        center = Offset(120f, 180f),
                        radius = 520f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ElectricBlue.copy(alpha = 0.22f),
                            Color.Transparent
                        ),
                        center = Offset(950f, 1150f),
                        radius = 680f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            PunkViolet.copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        start = Offset(0f, 900f),
                        end = Offset(950f, 0f)
                    )
                )
        )

        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            content()
        }
    }
}
