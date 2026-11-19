package com.Pexwilu8.Leqviw.presentation.menu

data class MenuUiState(
    val bestScore: Int = 0,
    val gems: Int = 0,
    val currentStage: Int = 0,
    val rewardsWaiting: Int = 0,
    val hasProgress: Boolean = false
)
