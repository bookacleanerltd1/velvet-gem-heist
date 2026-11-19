package com.Pexwilu8.Leqviw.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.SurfaceRaised
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextSecondary

@Composable
fun EmptyState(
    @StringRes messageRes: Int,
    @DrawableRes spriteRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(spriteRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(72.dp)
                .alpha(0.25f)
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(messageRes),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun SectionHeader(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(textRes),
        color = TextSecondary,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
    )
}

@Composable
fun ProgressBarBrass(
    fraction: Float,
    modifier: Modifier = Modifier,
    accent: Color = AccentGold
) {
    val safe = fraction.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(SurfaceRaised)
    ) {
        if (safe > 0f) {
        Box(
            modifier = Modifier
                .fillMaxWidth(safe)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Brush.horizontalGradient(listOf(accent.copy(alpha = 0.7f), accent)))
        )
        }
    }
}
