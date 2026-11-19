package com.Pexwilu8.Leqviw.presentation.game

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.theme.SheenTop
import com.Pexwilu8.Leqviw.core.theme.SurfaceBrass
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.BrassFrame
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.domain.model.Gem

@Composable
fun ClueCard(
    gem: Gem,
    clueIndex: Int,
    cluesPerRoom: Int,
    found: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val tint = GemVisuals.tint(gem)
    BrassFrame(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        thickness = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceBrass)
                .background(Brush.verticalGradient(listOf(SheenTop, Color.Transparent), endY = 34f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(GemVisuals.sprite(gem)),
                contentDescription = stringResource(R.string.cd_gem),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            ) {
                Text(
                    text = stringResource(R.string.clue_label, clueIndex + 1, cluesPerRoom),
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(R.string.clue_find, stringResource(GemVisuals.nameRes(gem))),
                    color = tint,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                repeat(total) { index ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = if (index < found) tint else Color(0xFF3A342A),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
