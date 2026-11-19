package com.Pexwilu8.Leqviw.domain.model

data class PlayerProgress(
    val bestScore: Int = 0,
    val gems: Int = 0,
    val unlockedStages: Int = 1,
    val stageStars: List<Int> = List(9) { 0 },
    val stageBest: List<Int> = List(9) { 0 },
    val gemLifted: List<Int> = List(5) { 0 },
    val totalLifted: Int = 0,
    val casesClosed: Int = 0,
    val bestChain: Int = 0,
    val cleanRooms: Int = 0,
    val missionTier: List<Int> = List(3) { 0 },
    val missionProgress: List<Int> = List(3) { 0 },
    val claimedRewards: Set<String> = emptySet(),
    val tutorialSeen: Boolean = false
)
