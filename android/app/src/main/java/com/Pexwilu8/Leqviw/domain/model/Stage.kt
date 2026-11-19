package com.Pexwilu8.Leqviw.domain.model

data class Stage(
    val index: Int,
    val wing: StageWing,
    val sweepMs: Long,
    val startingLockpicks: Int
)
