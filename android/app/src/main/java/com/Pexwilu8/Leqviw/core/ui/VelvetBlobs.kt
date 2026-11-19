package com.Pexwilu8.Leqviw.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold

@Composable
fun VelvetBlobs(
    modifier: Modifier = Modifier,
    amethystAlpha: Float = 0.12f,
    brassAlpha: Float = 0.08f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val radius = size.minDimension * 0.55f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(AccentAmethyst.copy(alpha = amethystAlpha), Color.Transparent),
                center = Offset(size.width * 0.18f, size.height * 0.22f),
                radius = radius
            ),
            radius = radius,
            center = Offset(size.width * 0.18f, size.height * 0.22f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(AccentGold.copy(alpha = brassAlpha), Color.Transparent),
                center = Offset(size.width * 0.86f, size.height * 0.74f),
                radius = radius * 0.8f
            ),
            radius = radius * 0.8f,
            center = Offset(size.width * 0.86f, size.height * 0.74f)
        )
        val dotStep = size.width / 14f
        var x = dotStep / 2f
        while (x < size.width) {
            var y = dotStep / 2f
            while (y < size.height) {
                drawCircle(
                    color = AccentGold.copy(alpha = 0.05f),
                    radius = 1.4f,
                    center = Offset(x, y)
                )
                y += dotStep
            }
            x += dotStep
        }
    }
}
