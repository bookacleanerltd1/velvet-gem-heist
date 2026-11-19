package com.Pexwilu8.Leqviw.presentation.levelmap

import com.Pexwilu8.Leqviw.domain.model.StageWing

data class StageNode(
    val index: Int,
    val wing: StageWing,
    val unlocked: Boolean,
    val stars: Int,
    val best: Int,
    val current: Boolean
)

data class LevelMapUiState(
    val nodes: List<StageNode> = emptyList(),
    val selected: Int = 0,
    val firstRun: Boolean = true
)
