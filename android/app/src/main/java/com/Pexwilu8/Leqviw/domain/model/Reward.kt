package com.Pexwilu8.Leqviw.domain.model

enum class RewardKind {
    BRASS_CASE,
    VELVET_POUCH,
    GILDED_CASKET
}

data class Reward(
    val kind: RewardKind,
    val goal: Int,
    val progress: Int,
    val gems: Int,
    val claimed: Boolean
) {
    val earned: Boolean get() = progress >= goal
}
