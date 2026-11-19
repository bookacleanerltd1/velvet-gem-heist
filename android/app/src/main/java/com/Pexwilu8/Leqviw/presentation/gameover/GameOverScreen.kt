package com.Pexwilu8.Leqviw.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.di.AppViewModelFactory
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.GemEmerald
import com.Pexwilu8.Leqviw.core.theme.GemRuby
import com.Pexwilu8.Leqviw.core.theme.GoldHighlight
import com.Pexwilu8.Leqviw.core.theme.TextOnGold
import com.Pexwilu8.Leqviw.core.theme.TextPrimary
import com.Pexwilu8.Leqviw.core.theme.TextSecondary
import com.Pexwilu8.Leqviw.core.ui.GemVisuals
import com.Pexwilu8.Leqviw.core.ui.PrimaryButton
import com.Pexwilu8.Leqviw.core.ui.ScreenBackground
import com.Pexwilu8.Leqviw.core.ui.SecondaryButton
import com.Pexwilu8.Leqviw.core.ui.StatCardData
import com.Pexwilu8.Leqviw.core.ui.StatRow
import com.Pexwilu8.Leqviw.core.ui.VelvetBlobs
import com.Pexwilu8.Leqviw.core.ui.VelvetSurface
import com.Pexwilu8.Leqviw.domain.model.CaseResult

private val closedOverlay = listOf(
    Color(0xE60B0B0F),
    Color(0xF21E1A14),
    Color(0xFA0B0B0F)
)

private val alarmOverlay = listOf(
    Color(0xE60B0B0F),
    Color(0xF22A1016),
    Color(0xFA0B0B0F)
)

@Composable
fun GameOverScreen(
    result: CaseResult,
    onPlayAgain: () -> Unit,
    onLevelMap: () -> Unit,
    onRewards: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(result) {
        gameOverViewModel.refresh()
    }

    val ribbonScale by animateFloatAsState(
        targetValue = if (result.newRecord) 1f else 0.9f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 260f),
        label = "recordRibbon"
    )

    Box(modifier = modifier.fillMaxSize()) {
        ScreenBackground(overlay = if (result.caseClosed) closedOverlay else alarmOverlay)
        VelvetBlobs(amethystAlpha = 0.13f, brassAlpha = 0.09f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(84.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(
                    if (result.caseClosed) R.string.case_closed else R.string.alarm_tripped
                ),
                color = if (result.caseClosed) GoldHighlight else GemRuby,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(
                    R.string.rooms_cleared,
                    stringResource(GemVisuals.wingRes(result.wing)),
                    result.roomsCleared,
                    result.roomsPerCase
                ),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            if (result.newRecord) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = ribbonScale
                            scaleY = ribbonScale
                        }
                        .background(AccentGold, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.new_record),
                        color = TextOnGold,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            VelvetSurface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = 12.dp,
                borderColor = AccentGold.copy(alpha = 0.4f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StatRow(
                        cards = listOf(
                            StatCardData(
                                labelRes = R.string.stat_score,
                                value = GemVisuals.groupedNumber(result.score),
                                accent = AccentGold
                            ),
                            StatCardData(
                                labelRes = R.string.stat_case,
                                value = (result.stageIndex + 1).toString(),
                                accent = GoldHighlight
                            ),
                            StatCardData(
                                labelRes = R.string.stat_lifted,
                                value = if (result.gemsLifted > 0) result.gemsLifted.toString() else "",
                                accent = GemEmerald
                            ),
                            StatCardData(
                                labelRes = R.string.stat_best_chain,
                                value = if (result.bestChain > 0) result.bestChain.toString() else "",
                                accent = AccentAmethyst
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_4),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                R.string.best_line,
                                GemVisuals.groupedNumber(state.bestScore),
                                GemVisuals.groupedNumber(state.totalGems)
                            ),
                            color = TextPrimary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    if (result.stars > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.stars_line, result.stars),
                            color = GoldHighlight,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            if (result.rewardsWaiting > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.reward_waiting, result.rewardsWaiting),
                    color = GemEmerald,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(
                    textRes = R.string.rewards,
                    onClick = onRewards,
                    leadingIcon = R.drawable.ic_reward,
                    accent = GemEmerald,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryButton(
                textRes = R.string.play_again,
                onClick = onPlayAgain,
                leadingIcon = R.drawable.ic_play,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                textRes = R.string.level_map,
                onClick = onLevelMap,
                leadingIcon = R.drawable.ic_map,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                textRes = R.string.menu,
                onClick = onMenu,
                leadingIcon = R.drawable.ic_home,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
