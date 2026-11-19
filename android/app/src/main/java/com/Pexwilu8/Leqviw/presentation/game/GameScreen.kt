package com.Pexwilu8.Leqviw.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.core.di.AppViewModelFactory
import com.Pexwilu8.Leqviw.core.theme.AccentAmethyst
import com.Pexwilu8.Leqviw.core.theme.AccentGold
import com.Pexwilu8.Leqviw.core.theme.GemRuby
import com.Pexwilu8.Leqviw.core.theme.Hairline
import com.Pexwilu8.Leqviw.core.theme.SurfaceVelvet
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
import com.Pexwilu8.Leqviw.presentation.dialog.BranchDialog
import com.Pexwilu8.Leqviw.presentation.dialog.ConfirmDialog
import com.Pexwilu8.Leqviw.presentation.dialog.PauseDialog

private val gameOverlay = listOf(
    Color(0xCC05050A),
    Color(0xD90B0B0F),
    Color(0xF2141018)
)

@Composable
fun GameScreen(
    stageIndex: Int,
    session: Int,
    onFinished: (CaseResult) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(session) {
        gameViewModel.startCase(stageIndex, session)
    }

    LaunchedEffect(Unit) {
        gameViewModel.results.collect { result ->
            onFinished(result)
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pauseRound() }
    }

    BackHandler(enabled = true) { gameViewModel.requestExit() }

    GameContent(
        state = state,
        onPlate = gameViewModel::liftPlate,
        onBlind = gameViewModel::goBlind,
        onLockpick = gameViewModel::useLockpick,
        onDeeper = gameViewModel::openBranch,
        onPause = gameViewModel::openPause,
        onHome = gameViewModel::requestExit,
        modifier = modifier
    )

    if (state.showBranch) {
        BranchDialog(doors = state.doors, onChoose = gameViewModel::chooseDoor)
    }
    if (state.showPause) {
        PauseDialog(
            onResume = gameViewModel::closePause,
            onRestart = {
                gameViewModel.closePause()
                gameViewModel.restartCase()
            },
            onMenu = {
                gameViewModel.pauseRound()
                onMenu()
            }
        )
    }
    if (state.showExit) {
        ConfirmDialog(
            titleRes = R.string.exit_title,
            bodyRes = R.string.exit_body,
            confirmRes = R.string.leave,
            onConfirm = {
                gameViewModel.dismissExit()
                gameViewModel.pauseRound()
                onMenu()
            },
            onDismiss = gameViewModel::dismissExit
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onPlate: (Int) -> Unit,
    onBlind: () -> Unit,
    onLockpick: () -> Unit,
    onDeeper: () -> Unit,
    onPause: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        ScreenBackground(overlay = gameOverlay)
        VelvetBlobs(amethystAlpha = 0.16f, brassAlpha = 0.07f)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state, onPause = onPause, onHome = onHome)
            Spacer(modifier = Modifier.height(12.dp))
            ClueCard(
                gem = state.clueGem,
                clueIndex = state.clueIndex,
                cluesPerRoom = state.cluesPerRoom,
                found = state.clueFound,
                total = GameConfig.PLATES_PER_CLUE,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            EvidenceBoard(
                state = state,
                onPlate = onPlate,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            StatStrip(state = state, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(10.dp))
            ControlPanel(
                state = state,
                onBlind = onBlind,
                onLockpick = onLockpick,
                onDeeper = onDeeper
            )
        }
    }
}

@Composable
private fun GameHeader(
    state: GameUiState,
    onPause: () -> Unit,
    onHome: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVelvet.copy(alpha = 0.92f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onHome),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_home),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    R.string.room_label,
                    state.room,
                    GameConfig.ROOMS_PER_CASE
                ),
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = stringResource(GemVisuals.wingRes(state.wing)),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = onPause),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(AccentGold.copy(alpha = 0.2f))
    )
}

@Composable
private fun StatStrip(state: GameUiState, modifier: Modifier = Modifier) {
    VelvetSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = 6.dp,
        borderColor = Hairline
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatRow(
                cards = buildStatCards(state),
                compact = true,
                modifier = Modifier.weight(1f)
            )
            SuspicionPips(
                filled = state.suspicion,
                limit = state.suspicionLimit,
                reducedMotion = state.reducedMotion
            )
        }
    }
}

@Composable
private fun buildStatCards(state: GameUiState): List<StatCardData> {
    val cards = mutableListOf<StatCardData>()
    cards.add(
        StatCardData(
            labelRes = R.string.stat_score,
            value = GemVisuals.groupedNumber(state.score),
            accent = AccentGold,
            announce = true
        )
    )
    if (state.chain > 0) {
        cards.add(
            StatCardData(
                labelRes = R.string.stat_chain,
                value = stringResource(R.string.chain_value, state.chainMultiplier),
                accent = AccentAmethyst
            )
        )
    }
    cards.add(
        StatCardData(
            labelRes = R.string.stat_lockpicks,
            value = state.lockpicks.toString(),
            accent = TextSecondary
        )
    )
    return cards
}

@Composable
private fun SuspicionPips(filled: Int, limit: Int, reducedMotion: Boolean) {
    val pulse by animateFloatAsState(
        targetValue = if (filled >= limit - 1 && !reducedMotion) 1.3f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 320f),
        label = "suspicionPulse"
    )
    val label = stringResource(R.string.stat_suspicion)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics {
            liveRegion = LiveRegionMode.Polite
            stateDescription = "$filled / $limit"
        }
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            repeat(limit) { index ->
                val active = index < filled
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .graphicsLayer {
                            val factor = if (active && index == filled - 1) pulse else 1f
                            scaleX = factor
                            scaleY = factor
                        }
                        .background(
                            color = if (active) GemRuby else Color(0xFF3A2A2E),
                            shape = CircleShape
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = label,
            color = TextSecondary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun ControlPanel(
    state: GameUiState,
    onBlind: () -> Unit,
    onLockpick: () -> Unit,
    onDeeper: () -> Unit
) {
    VelvetSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        elevation = 16.dp,
        borderColor = AccentGold.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    if (state.phase == GamePhase.SWEEP) R.string.hint_sweep else R.string.hint_lift
                ),
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.roomCleared) {
                    PrimaryButton(
                        textRes = R.string.go_deeper,
                        onClick = onDeeper,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    )
                } else {
                    PrimaryButton(
                        textRes = R.string.go_blind,
                        onClick = onBlind,
                        leadingIcon = R.drawable.ic_gem,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(56.dp)
                    )
                    SecondaryButton(
                        textRes = R.string.lockpick,
                        onClick = onLockpick,
                        enabled = state.lockpicks > 0 && state.phase == GamePhase.IDLE,
                        leadingIcon = R.drawable.ic_pick,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .semantics { stateDescription = state.lockpicks.toString() }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
