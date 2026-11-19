package com.Pexwilu8.Leqviw.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.SurfaceRaised
import com.Pexwilu8.Leqviw.core.theme.TextPrimary

@Composable
fun TileButton(
    @StringRes textRes: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = AccentGold
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 420f),
        label = "tilePress"
    )
    OutlinedButton(
        onClick = onClick,
        interactionSource = interaction,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.28f)),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceRaised.copy(alpha = 0.9f),
            contentColor = TextPrimary
        ),
        modifier = modifier
            .defaultMinSize(minHeight = 62.dp)
            .height(62.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(textRes),
                fontSize = 10.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
