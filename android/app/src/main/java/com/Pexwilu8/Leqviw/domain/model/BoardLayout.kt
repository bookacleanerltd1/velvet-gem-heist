package com.Pexwilu8.Leqviw.domain.model

import com.Pexwilu8.Leqviw.core.config.GameConfig

data class BoardLayout(
    val gems: List<Gem>,
    val clueGems: List<Gem>,
    val seed: Long,
    val fromFallback: Boolean
)

object FallbackLayout {
    val plan: BoardLayout = BoardLayout(
        gems = listOf(
            Gem.RUBY, Gem.SAPPHIRE, Gem.TOPAZ, Gem.EMERALD,
            Gem.AMETHYST, Gem.RUBY, Gem.EMERALD, Gem.SAPPHIRE,
            Gem.EMERALD, Gem.TOPAZ, Gem.RUBY, Gem.AMETHYST,
            Gem.SAPPHIRE, Gem.AMETHYST, Gem.TOPAZ, Gem.SAPPHIRE
        ),
        clueGems = listOf(Gem.RUBY, Gem.EMERALD),
        seed = 0L,
        fromFallback = true
    )
}

fun BoardLayout.isSolvable(): Boolean {
    if (gems.size != GameConfig.PLATE_COUNT) return false
    if (clueGems.size != GameConfig.CLUES_PER_ROOM) return false
    if (clueGems.toSet().size != clueGems.size) return false
    clueGems.forEach { clue ->
        if (gems.count { it == clue } != GameConfig.PLATES_PER_CLUE) return false
    }
    val decoys = Gem.entries.filter { it !in clueGems }
    decoys.forEach { decoy ->
        if (gems.count { it == decoy } > GameConfig.MAX_DECOY_PER_FAMILY) return false
    }
    return true
}
