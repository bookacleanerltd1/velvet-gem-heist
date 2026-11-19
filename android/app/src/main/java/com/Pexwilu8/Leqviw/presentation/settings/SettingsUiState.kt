package com.Pexwilu8.Leqviw.presentation.settings

import com.Pexwilu8.Leqviw.domain.model.GameSettings

data class SettingsUiState(
    val settings: GameSettings = GameSettings(),
    val showReset: Boolean = false,
    val gems: Int = 0,
    val casesClosed: Int = 0
)
