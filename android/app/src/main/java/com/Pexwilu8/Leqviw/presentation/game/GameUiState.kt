package com.Pexwilu8.Leqviw.presentation.game

import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.domain.model.BranchDoor
import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.model.StageWing

enum class GamePhase {
    SWEEP,
    IDLE,
    REVEALING,
    CLUE_SOLVED,
    ROOM_CLEARED,
    ALARM,
    CASE_CLOSED
}

data class PlateView(
    val index: Int,
    val gem: Gem,
    val revealed: Boolean = false,
    val locked: Boolean = false,
    val marked: Boolean = false
)

data class GameUiState(
    val stageIndex: Int = 0,
    val wing: StageWing = StageWing.VELVET_LOUNGE,
    val room: Int = 1,
    val plates: List<PlateView> = emptyList(),
    val clueGems: List<Gem> = listOf(Gem.RUBY, Gem.EMERALD),
    val clueIndex: Int = 0,
    val clueFound: Int = 0,
    val score: Int = 0,
    val chain: Int = 0,
    val chainMultiplier: Float = 1f,
    val bestChain: Int = 0,
    val suspicion: Int = 0,
    val suspicionLimit: Int = GameConfig.MAX_SUSPICION,
    val lockpicks: Int = 0,
    val gemsLifted: Int = 0,
    val gemsEarned: Int = 0,
    val roomMultiplier: Int = 1,
    val phase: GamePhase = GamePhase.SWEEP,
    val doors: List<BranchDoor> = listOf(BranchDoor.VELVET_CURTAIN, BranchDoor.SERVICE_HALL),
    val showBranch: Boolean = false,
    val showPause: Boolean = false,
    val showExit: Boolean = false,
    val hintIndex: Int = -1,
    val sweepMs: Long = GameConfig.SWEEP_BASE_MS,
    val chalkEnabled: Boolean = true,
    val hintEnabled: Boolean = true,
    val reducedMotion: Boolean = false,
    val seed: Long = 0L
) {
    val clueGem: Gem get() = clueGems.getOrElse(clueIndex) { Gem.RUBY }
    val cluesPerRoom: Int get() = clueGems.size
    val boardLocked: Boolean get() = phase != GamePhase.IDLE && phase != GamePhase.SWEEP
    val roomCleared: Boolean get() = phase == GamePhase.ROOM_CLEARED
}
