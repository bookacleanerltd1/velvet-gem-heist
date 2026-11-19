package com.Pexwilu8.Leqviw.presentation.rewards

import com.Pexwilu8.Leqviw.domain.model.Reward

data class RewardsUiState(
    val rewards: List<Reward> = emptyList(),
    val gems: Int = 0
) {
    val anyAvailable: Boolean get() = rewards.any { it.progress > 0 || it.claimed }
}
