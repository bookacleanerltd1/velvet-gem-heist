package com.Pexwilu8.Leqviw.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.Hairline
import com.Pexwilu8.Leqviw.core.theme.SurfaceRaised
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.di.AppViewModelFactory
import com.Pexwilu8.Leqviw.core.ui.EmptyState
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.core.ui.ProgressBarBrass
import com.Pexwilu8.Leqviw.core.ui.ScreenScaffold
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface
import com.Pexwilu8.Leqviw.domain.model.CollectionEntry

@Composable
fun CollectionScreen(
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        collectionViewModel.refresh()
    }

    ScreenScaffold(
        titleRes = R.string.collection,
        subtitleRes = R.string.collection_subtitle,
        onMenu = onMenu,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.collection_header, state.discovered, state.total),
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProgressBarBrass(
            fraction = if (state.total > 0) state.discovered.toFloat() / state.total else 0f
        )
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.empty) {
                EmptyState(
                    messageRes = R.string.collection_empty,
                    spriteRes = R.drawable.sprite_3
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.entries.chunked(3).forEachIndexed { rowIndex, row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEachIndexed { columnIndex, entry ->
                                val index = rowIndex * 3 + columnIndex
                                StoneCell(
                                    entry = entry,
                                    selected = index == state.selected,
                                    onClick = { collectionViewModel.select(index) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
        val detail = state.detail
        if (detail != null) {
            Spacer(modifier = Modifier.height(10.dp))
            VelvetSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                borderColor = GemVisuals.tint(detail.gem).copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            R.string.stone_title,
                            stringResource(GemVisuals.cutRes(detail.cut)),
                            stringResource(GemVisuals.nameRes(detail.gem))
                        ),
                        color = GemVisuals.tint(detail.gem),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            R.string.stone_note,
                            stringResource(GemVisuals.wingRes(detail.wing))
                        ),
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.lifted_count, detail.lifted),
                        color = AccentGold,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun StoneCell(
    entry: CollectionEntry,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = GemVisuals.tint(entry.gem)
    val stateLabel = stringResource(
        if (entry.discovered) R.string.unlocked else R.string.locked
    )
    Column(
        modifier = modifier
            .height(62.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (entry.discovered) SurfaceRaised else Color(0xFF17131C))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (entry.discovered) tint.copy(alpha = if (selected) 1f else 0.4f) else Hairline,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(enabled = entry.discovered, onClick = onClick)
            .padding(6.dp)
            .semantics { stateDescription = stateLabel },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (entry.discovered) {
            Image(
                painter = painterResource(GemVisuals.sprite(entry.gem)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(30.dp)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_plate),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .alpha(0.3f)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(GemVisuals.cutRes(entry.cut)),
            color = if (entry.discovered) tint else TextMuted,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}
