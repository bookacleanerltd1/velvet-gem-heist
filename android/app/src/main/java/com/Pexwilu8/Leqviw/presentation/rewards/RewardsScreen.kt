package com.Pexwilu8.Leqviw.presentation.rewards

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Pexwilu8.Leqviw.core.theme.GemEmerald
import com.Pexwilu8.Leqviw.core.theme.GoldHighlight
import com.Pexwilu8.Leqviw.core.theme.TextMuted
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.EmptyState
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.core.ui.ProgressBarBrass
import com.Pexwilu8.Leqviw.core.ui.ScreenScaffold
import com.Pexwilu8.Leqviw.core.ui.SecondaryButton
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface
import com.Pexwilu8.Leqviw.domain.model.Reward

@Composable
fun RewardsScreen(
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = AppViewModelFactory)
    val state by rewardsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rewardsViewModel.refresh()
    }

    ScreenScaffold(
        titleRes = R.string.rewards,
        subtitleRes = R.string.rewards_subtitle,
        onMenu = onMenu,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_gem),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.gems_total, GemVisuals.groupedNumber(state.gems)),
                color = GoldHighlight,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.anyAvailable) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.rewards.forEach { reward ->
                        CrateCard(
                            reward = reward,
                            onClaim = { rewardsViewModel.claim(reward.kind) }
                        )
                    }
                }
            } else {
                EmptyState(
                    messageRes = R.string.rewards_empty,
                    spriteRes = R.drawable.sprite_4
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.rewards_footer),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CrateCard(reward: Reward, onClaim: () -> Unit) {
    val fraction = if (reward.goal > 0) reward.progress.toFloat() / reward.goal else 0f
    val stateLabel = stringResource(
        when {
            reward.claimed -> R.string.claimed
            reward.earned -> R.string.claim
            else -> R.string.locked
        }
    )
    VelvetSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = 10.dp,
        borderColor = if (reward.earned && !reward.claimed) GemEmerald.copy(alpha = 0.6f) else AccentGold.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .semantics { stateDescription = stateLabel },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(GemVisuals.rewardIcon(reward.kind)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(GemVisuals.rewardRes(reward.kind)),
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(GemVisuals.rewardGoalRes(reward.kind)),
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgressBarBrass(
                    fraction = fraction,
                    accent = if (reward.earned) GemEmerald else AccentGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.progress_of,
                        reward.progress.coerceAtMost(reward.goal),
                        reward.goal
                    ),
                    color = TextMuted,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            SecondaryButton(
                textRes = if (reward.claimed) R.string.claimed else R.string.claim,
                onClick = onClaim,
                enabled = reward.earned && !reward.claimed,
                accent = GemEmerald
            )
        }
    }
}
