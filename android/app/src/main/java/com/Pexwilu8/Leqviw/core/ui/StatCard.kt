package com.Pexwilu8.Leqviw.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Pexwilu8.Leqviw.core.theme.TextSecondary

@Composable
fun StatCard(
    @StringRes labelRes: Int,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier,
    announce: Boolean = false,
    compact: Boolean = false
) {
    if (value.isBlank()) return
    VelvetSurface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        elevation = 6.dp,
        borderColor = accent.copy(alpha = 0.34f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = if (compact) 6.dp else 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(if (compact) 6.dp else 8.dp)
                    .background(accent, CircleShape)
            )
            Spacer(modifier = Modifier.height(if (compact) 3.dp else 6.dp))
            Text(
                text = value,
                color = accent,
                fontSize = if (compact) 16.sp else 20.sp,
                lineHeight = if (compact) 19.sp else 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = if (announce) {
                    Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                } else {
                    Modifier
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(labelRes),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun StatRow(
    cards: List<StatCardData>,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val visible = cards.filter { it.value.isNotBlank() }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        visible.forEach { card ->
            StatCard(
                labelRes = card.labelRes,
                value = card.value,
                accent = card.accent,
                announce = card.announce,
                compact = compact,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

data class StatCardData(
    val labelRes: Int,
    val value: String,
    val accent: Color,
    val announce: Boolean = false
)
