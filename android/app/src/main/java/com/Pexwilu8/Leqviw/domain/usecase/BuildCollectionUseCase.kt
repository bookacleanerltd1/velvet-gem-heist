package com.Pexwilu8.Leqviw.domain.usecase

import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.domain.repository.CatalogRepository
import com.Pexwilu8.Leqviw.domain.model.CollectionEntry
import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.model.GemCut
import com.Pexwilu8.Leqviw.domain.model.PlayerProgress

class BuildCollectionUseCase(private val catalog: CatalogRepository) {

    operator fun invoke(progress: PlayerProgress): List<CollectionEntry> =
        Gem.entries.flatMap { gem ->
            val lifted = progress.gemLifted.getOrElse(gem.ordinal) { 0 }
            GemCut.entries.map { cut ->
                CollectionEntry(
                    gem = gem,
                    cut = cut,
                    discovered = lifted >= threshold(cut),
                    lifted = lifted,
                    wing = catalog.homeWing(gem)
                )
            }
        }

    private fun threshold(cut: GemCut): Int = when (cut) {
        GemCut.ROUGH -> GameConfig.CUT_ROUGH_AT
        GemCut.CUT -> GameConfig.CUT_CUT_AT
        GemCut.FLAWLESS -> GameConfig.CUT_FLAWLESS_AT
    }
}
