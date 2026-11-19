package com.Pexwilu8.Leqviw.domain.model

enum class GemCut {
    ROUGH,
    CUT,
    FLAWLESS
}

data class CollectionEntry(
    val gem: Gem,
    val cut: GemCut,
    val discovered: Boolean,
    val lifted: Int,
    val wing: StageWing
)
