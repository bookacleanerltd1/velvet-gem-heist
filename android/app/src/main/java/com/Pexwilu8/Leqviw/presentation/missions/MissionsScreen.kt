package com.Pexwilu8.Leqviw.presentation.missions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Pexwilu8.Leqviw.core.theme.GemEmerald
import com.Pexwilu8.Leqviw.core.theme.GoldHighlight
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.ui.EmptyState
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.core.ui.ProgressBarBrass
import com.Pexwilu8.Leqviw.core.ui.ScreenScaffold
import com.Pexwilu8.Leqviw.core.ui.SecondaryButton
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface
import com.Pexwilu8.Leqviw.domain.model.Mission

@Composable
fun MissionsScreen(
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val missionsViewModel: MissionsViewModel = viewModel(factory = AppViewModelFactory)
    val state by missionsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        missionsViewModel.refresh()
    }

    ScreenScaffold(
        titleRes = R.string.missions,
        subtitleRes = R.string.missions_subtitle,
        onMenu = onMenu,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.gems_total, GemVisuals.groupedNumber(state.gems)),
            color = GoldHighlight,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.missions.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.missions.forEachIndexed { index, mission ->
                        MissionCard(
                            mission = mission,
                            onClaim = { missionsViewModel.claim(index) }
                        )
                    }
                }
            } else {
                EmptyState(
                    messageRes = R.string.missions_empty,
                    spriteRes = R.drawable.sprite_2
                )
            }
        }
    }
}

@Composable
private fun MissionCard(mission: Mission, onClaim: () -> Unit) {
    val accent = GemVisuals.missionAccent(mission.kind)
    val fraction = if (mission.target > 0) mission.progress.toFloat() / mission.target else 0f
    val stateLabel = stringResource(if (mission.complete) R.string.claim else R.string.locked)
    VelvetSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = 10.dp,
        borderColor = accent.copy(alpha = if (mission.complete) 0.7f else 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .semantics { stateDescription = stateLabel },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(accent)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = missionTitle(mission),
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgressBarBrass(fraction = fraction, accent = accent)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.progress_of,
                        mission.progress.coerceAtMost(mission.target),
                        mission.target
                    ),
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.mission_reward, mission.reward),
                    color = AccentGold,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SecondaryButton(
                textRes = R.string.claim,
                onClick = onClaim,
                enabled = mission.complete,
                accent = GemEmerald,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Composable
private fun missionTitle(mission: Mission): String =
    stringResource(GemVisuals.missionRes(mission.kind), mission.target)
