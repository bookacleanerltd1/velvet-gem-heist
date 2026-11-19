package com.Pexwilu8.Leqviw.presentation.missions

import com.Pexwilu8.Leqviw.domain.model.Mission

data class MissionsUiState(
    val missions: List<Mission> = emptyList(),
    val gems: Int = 0
) {
    val anyActive: Boolean get() = missions.isNotEmpty()
}
