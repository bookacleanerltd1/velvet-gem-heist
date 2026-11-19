package com.Pexwilu8.Leqviw.core.navigation

import com.Pexwilu8.Leqviw.domain.model.CaseResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val stageIndex: Int, val session: Int) : Screen
    data class GameOver(val result: CaseResult) : Screen
    data object LevelMap : Screen
    data object Tutorial : Screen
    data object Rewards : Screen
    data object Collection : Screen
    data object Missions : Screen
    data object Settings : Screen
}
