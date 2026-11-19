package com.Pexwilu8.Leqviw.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VelvetColorScheme = darkColorScheme(
    primary = AccentGold,
    onPrimary = TextOnGold,
    primaryContainer = SurfaceBrass,
    onPrimaryContainer = GoldHighlight,
    secondary = AccentAmethyst,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceRaised,
    onSecondaryContainer = TextPrimary,
    tertiary = GoldHighlight,
    onTertiary = TextOnGold,
    background = BackgroundDeep,
    onBackground = TextPrimary,
    surface = SurfaceVelvet,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = BrassDeep,
    outlineVariant = Hairline,
    error = GemRuby,
    onError = TextPrimary,
    scrim = ShadowDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VelvetColorScheme,
        typography = AppTypography,
        content = content
    )
}
