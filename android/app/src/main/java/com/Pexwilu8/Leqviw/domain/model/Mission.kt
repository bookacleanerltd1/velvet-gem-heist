package com.Pexwilu8.Leqviw.domain.model

enum class MissionKind {
    LIFT_STONES,
    CLEAN_ROOM,
    CHAIN_RUN
}

data class Mission(
    val kind: MissionKind,
    val tier: Int,
    val target: Int,
    val progress: Int,
    val reward: Int
) {
    val complete: Boolean get() = progress >= target
}
