package com.Pexwilu8.Leqviw.presentation.levelmap

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.graphicsLayer
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
import com.Pexwilu8.Leqviw.core.di.AppViewModelFactory
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.GoldHighlight
import com.Pexwilu8.Leqviw.core.theme.Hairline
import com.Pexwilu8.Leqviw.core.theme.SurfaceRaised
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.core.ui.PrimaryButton
import com.Pexwilu8.Leqviw.core.ui.ScreenScaffold
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface
import com.Pexwilu8.Leqviw.domain.model.Gem

@Composable
fun LevelMapScreen(
    onPlay: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val levelMapViewModel: LevelMapViewModel = viewModel(factory = AppViewModelFactory)
    val state by levelMapViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        levelMapViewModel.refresh()
    }

    ScreenScaffold(
        titleRes = R.string.level_map,
        subtitleRes = R.string.level_map_subtitle,
        onMenu = onMenu,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.nodes.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { node ->
                        StageNodeTile(
                            node = node,
                            selected = node.index == state.selected,
                            onSelect = { levelMapViewModel.select(node.index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            if (state.firstRun) {
                Text(
                    text = stringResource(R.string.levelmap_empty),
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            val selectedNode = state.nodes.getOrNull(state.selected)
            if (selectedNode != null) {
                VelvetSurface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = 10.dp,
                    borderColor = AccentGold.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(GemVisuals.wingRes(selectedNode.wing)),
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.enter_case, selectedNode.index + 1),
                            color = TextSecondary,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(
                                R.string.stage_best,
                                GemVisuals.groupedNumber(selectedNode.best)
                            ),
                            color = GoldHighlight,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PrimaryButton(
                            textRes = R.string.play,
                            onClick = { onPlay(selectedNode.index) },
                            leadingIcon = R.drawable.ic_play,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StageNodeTile(
    node: StageNode,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "nodePulse")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nodeScale"
    )
    val stateLabel = stringResource(
        if (node.unlocked) R.string.unlocked else R.string.locked
    )
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = node.unlocked, onClick = onSelect)
            .padding(vertical = 6.dp)
            .semantics { stateDescription = stateLabel },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(66.dp)
                .graphicsLayer {
                    val factor = if (node.current && node.unlocked) pulse else 1f
                    scaleX = factor
                    scaleY = factor
                }
                .clip(CircleShape)
                .background(if (node.unlocked) SurfaceRaised else Color(0xFF15151B))
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (node.unlocked) AccentGold.copy(alpha = if (selected) 1f else 0.4f) else Hairline,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (node.unlocked) {
                Image(
                    painter = painterResource(GemVisuals.sprite(Gem.entries[node.index % Gem.entries.size])),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .alpha(0.5f)
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.stage_label, node.index + 1),
            color = if (node.unlocked) TextSecondary else TextMuted,
            style = MaterialTheme.typography.labelSmall
        )
        if (node.stars > 0) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(node.stars) {
                    Image(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}
