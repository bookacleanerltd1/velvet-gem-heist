package com.Pexwilu8.Leqviw.domain.model

data class CaseResult(
    val stageIndex: Int,
    val wing: StageWing,
    val score: Int,
    val gemsLifted: Int,
    val bestChain: Int,
    val roomsCleared: Int,
    val roomsPerCase: Int,
    val caseClosed: Boolean,
    val newRecord: Boolean,
    val stars: Int,
    val rewardsWaiting: Int
)
