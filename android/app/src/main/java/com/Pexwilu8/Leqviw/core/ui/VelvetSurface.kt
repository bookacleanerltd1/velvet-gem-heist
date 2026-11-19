package com.Pexwilu8.Leqviw.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.BrassDeep
import com.Pexwilu8.Leqviw.core.theme.Hairline
import com.Pexwilu8.Leqviw.core.theme.PlateVelvetLow
import com.Pexwilu8.Leqviw.core.theme.ShadowDeep
import com.Pexwilu8.Leqviw.core.theme.SheenTop
import com.Pexwilu8.Leqviw.core.theme.SurfaceVelvet

@Composable
fun VelvetSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    elevation: Dp = 10.dp,
    borderColor: Color = Hairline,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier.shadow(elevation, shape, ambientColor = ShadowDeep, spotColor = ShadowDeep),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(SurfaceVelvet, PlateVelvetLow)
                    )
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(SheenTop, Color.Transparent),
                        endY = 48f
                    )
                ),
            content = content
        )
    }
}

@Composable
fun ScreenBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
    }
}

@Composable
fun BrassFrame(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    thickness: Dp = 2.dp,
    alpha: Float = 1f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .border(
                width = thickness,
                brush = Brush.verticalGradient(
                    listOf(
                        AccentGold.copy(alpha = alpha),
                        BrassDeep.copy(alpha = alpha)
                    )
                ),
                shape = shape
            ),
        content = content
    )
}
