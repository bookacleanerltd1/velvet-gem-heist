package com.Pexwilu8.Leqviw.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.GoldHighlight
import com.Pexwilu8.Leqviw.core.theme.Hairline
import com.Pexwilu8.Leqviw.core.theme.SurfaceRaised
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextOnGold
import com.Pexwilu8.Leqviw.core.theme.TextPrimary

@Composable
fun PrimaryButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes leadingIcon: Int? = null
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 420f),
        label = "primaryPress"
    )
    val shape = RoundedCornerShape(18.dp)
    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextOnGold,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextMuted
        ),
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (enabled) 1f else 0.38f
            }
            .shadow(
                elevation = if (enabled) 12.dp else 0.dp,
                shape = shape,
                ambientColor = AccentGold,
                spotColor = AccentGold
            )
            .clip(shape)
            .background(
                if (enabled) {
                    Brush.horizontalGradient(listOf(AccentGold, GoldHighlight))
                } else {
                    Brush.horizontalGradient(listOf(SurfaceRaised, SurfaceRaised))
                }
            )
    ) {
        if (leadingIcon != null) {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                tint = TextOnGold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            text = stringResource(textRes),
            fontSize = 17.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.6.sp
        )
    }
}

@Composable
fun SecondaryButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes leadingIcon: Int? = null,
    accent: Color = AccentGold
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 420f),
        label = "secondaryPress"
    )
    val shape = RoundedCornerShape(14.dp)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = shape,
        border = BorderStroke(1.dp, if (enabled) accent.copy(alpha = 0.45f) else Hairline),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceRaised.copy(alpha = 0.92f),
            contentColor = TextPrimary,
            disabledContainerColor = SurfaceRaised.copy(alpha = 0.45f),
            disabledContentColor = TextMuted
        ),
        modifier = modifier
            .defaultMinSize(minHeight = 52.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (enabled) 1f else 0.38f
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    painter = painterResource(leadingIcon),
                    contentDescription = null,
                    tint = if (enabled) accent else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = stringResource(textRes),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }
    }
}
