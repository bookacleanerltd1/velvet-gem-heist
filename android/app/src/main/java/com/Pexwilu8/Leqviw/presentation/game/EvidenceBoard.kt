package com.Pexwilu8.Leqviw.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.core.theme.AlarmTint
import com.Pexwilu8.Leqviw.core.theme.ShadowDeep
import com.Pexwilu8.Leqviw.core.theme.SurfaceBrass
import com.Pexwilu8.Leqviw.core.ui.BrassFrame
import com.Pexwilu8.Leqviw.core.ui.GemPlate

private val BoardMaxWidth = 340.dp
private val BoardPad = 8.dp
private val BoardBorder = 2.dp
private val BoardGap = 8.dp

@Composable
fun EvidenceBoard(
    state: GameUiState,
    onPlate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val boardDim by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.ROOM_CLEARED || state.phase == GamePhase.CASE_CLOSED) 0.4f else 1f,
        animationSpec = tween(durationMillis = 280),
        label = "boardDim"
    )
    val alarmFlash by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.ALARM) 0.35f else 0f,
        animationSpec = tween(durationMillis = 160),
        label = "alarmFlash"
    )
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val frame = BoardPad + BoardBorder
        val columns = GameConfig.BOARD_COLS
        val rows = GameConfig.BOARD_ROWS
        val chrome = frame * 2
        val availableWidth = min(maxWidth - 32.dp, BoardMaxWidth)
        val availableHeight = maxHeight - 8.dp
        val tileFromWidth = (availableWidth - chrome - BoardGap * (columns - 1)) / columns
        val tileFromHeight = (availableHeight - chrome - BoardGap * (rows - 1)) / rows
        val tile = min(tileFromWidth, tileFromHeight).coerceIn(48.dp, 82.dp)
        val boardWidth = tile * columns + BoardGap * (columns - 1) + chrome
        BrassFrame(
            modifier = Modifier
                .width(boardWidth)
                .shadow(14.dp, RoundedCornerShape(18.dp), ambientColor = ShadowDeep, spotColor = ShadowDeep),
            shape = RoundedCornerShape(18.dp),
            thickness = BoardBorder,
            alpha = 0.6f
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceBrass)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF221C14), Color(0xFF141018)))
                    )
                    .padding(BoardPad)
                    .alpha(boardDim)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(BoardGap)) {
                    repeat(GameConfig.BOARD_ROWS) { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(BoardGap)) {
                            repeat(columns) { column ->
                                val index = row * columns + column
                                val plate = state.plates.getOrNull(index)
                                if (plate != null) {
                                    GemPlate(
                                        gem = plate.gem,
                                        revealed = plate.revealed || state.phase == GamePhase.SWEEP,
                                        locked = plate.locked,
                                        marked = plate.marked && state.chalkEnabled,
                                        hinted = state.hintIndex == index,
                                        tileSize = tile,
                                        reducedMotion = state.reducedMotion,
                                        enabled = !state.boardLocked && !state.showPause && !state.showExit,
                                        plateNumber = index + 1,
                                        onClick = { onPlate(index) }
                                    )
                                } else {
                                    Box(modifier = Modifier.width(tile))
                                }
                            }
                        }
                    }
                }
            }
        }
        if (alarmFlash > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AlarmTint.copy(alpha = alarmFlash))
            )
        }
    }
}
