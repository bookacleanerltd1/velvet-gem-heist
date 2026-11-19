package com.Pexwilu8.Leqviw.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.PlateVelvetHigh
import com.Pexwilu8.Leqviw.core.theme.PlateVelvetLow
import com.Pexwilu8.Leqviw.core.theme.SheenTop
import com.Pexwilu8.Leqviw.core.theme.SurfaceBrass
import com.Pexwilu8.Leqviw.domain.model.Gem

@Composable
fun GemPlate(
    gem: Gem,
    revealed: Boolean,
    locked: Boolean,
    marked: Boolean,
    hinted: Boolean,
    tileSize: Dp,
    reducedMotion: Boolean,
    enabled: Boolean,
    plateNumber: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = GemVisuals.tint(gem)
    val shape = RoundedCornerShape(12.dp)
    val rotation by animateFloatAsState(
        targetValue = if (revealed) 180f else 0f,
        animationSpec = if (reducedMotion) snap() else tween(durationMillis = 320),
        label = "plateFlip"
    )
    val glow by animateFloatAsState(
        targetValue = if (locked) 0.45f else 0f,
        animationSpec = tween(durationMillis = 260),
        label = "plateGlow"
    )
    val gemName = stringResource(GemVisuals.nameRes(gem))
    val hiddenLabel = stringResource(R.string.plate_hidden)
    val lockedLabel = stringResource(R.string.plate_locked)
    val markedLabel = stringResource(R.string.plate_marked)
    val plateLabel = stringResource(R.string.plate_number, plateNumber)
    val state = when {
        locked -> lockedLabel
        marked -> markedLabel
        else -> hiddenLabel
    }
    Box(
        modifier = modifier
            .size(tileSize)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14f * density
            }
            .clip(shape)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics {
                contentDescription = plateLabel
                stateDescription = if (locked) "$gemName $lockedLabel" else state
            },
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90f) {
            HiddenPlateFace(
                shape = shape,
                marked = marked,
                markTint = tint,
                hinted = hinted,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                contentAlignment = Alignment.Center
            ) {
                RevealedPlateFace(
                    gem = gem,
                    tint = tint,
                    shape = shape,
                    locked = locked,
                    glow = glow,
                    tileSize = tileSize
                )
            }
        }
    }
}

@Composable
private fun HiddenPlateFace(
    shape: RoundedCornerShape,
    marked: Boolean,
    markTint: Color,
    hinted: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        hinted -> AccentGold
        marked -> markTint.copy(alpha = 0.6f)
        else -> AccentAmethyst.copy(alpha = 0.24f)
    }
    Box(
        modifier = modifier
            .clip(shape)
            .background(Brush.linearGradient(listOf(PlateVelvetHigh, PlateVelvetLow)))
            .background(Brush.verticalGradient(listOf(SheenTop, Color.Transparent), endY = 18f))
            .border(if (hinted || marked) 2.dp else 1.dp, borderColor, shape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_plate),
            contentDescription = null,
            modifier = Modifier
                .size(22.dp)
                .alpha(if (marked) 0.55f else 0.3f)
        )
    }
}

@Composable
private fun RevealedPlateFace(
    gem: Gem,
    tint: Color,
    shape: RoundedCornerShape,
    locked: Boolean,
    glow: Float,
    tileSize: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(SurfaceBrass)
            .background(
                Brush.radialGradient(
                    listOf(tint.copy(alpha = 0.18f + glow * 0.3f), Color.Transparent)
                )
            )
            .border(2.dp, tint.copy(alpha = if (locked) 0.95f else 0.5f), shape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(GemVisuals.sprite(gem)),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(tileSize * 0.62f)
                .padding(2.dp)
        )
        if (locked) {
            Image(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(14.dp)
            )
        }
    }
}
