package com.Pexwilu8.Leqviw.domain.usecase

import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.domain.model.BoardLayout
import com.Pexwilu8.Leqviw.domain.model.FallbackLayout
import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.model.isSolvable
import kotlin.random.Random

class GenerateBoardUseCase {

    operator fun invoke(seed: Long): BoardLayout {
        val random = Random(seed)
        repeat(GameConfig.GEN_ATTEMPTS) {
            val candidate = build(random, seed)
            if (candidate.isSolvable()) return candidate
        }
        return FallbackLayout.plan
    }

    private fun build(random: Random, seed: Long): BoardLayout {
        val families = Gem.entries.shuffled(random)
        val clues = families.take(GameConfig.CLUES_PER_ROOM)
        val decoys = families.drop(GameConfig.CLUES_PER_ROOM)
        val counts = decoySplit(random, decoys.size)
        val pool = ArrayList<Gem>(GameConfig.PLATE_COUNT)
        clues.forEach { clue -> repeat(GameConfig.PLATES_PER_CLUE) { pool.add(clue) } }
        decoys.forEachIndexed { index, gem -> repeat(counts[index]) { pool.add(gem) } }
        return BoardLayout(
            gems = pool.shuffled(random),
            clueGems = clues,
            seed = seed,
            fromFallback = false
        )
    }

    private fun decoySplit(random: Random, families: Int): List<Int> {
        val counts = MutableList(families) { 1 }
        var left = GameConfig.DECOY_COUNT - families
        while (left > 0) {
            val open = counts.indices.filter { counts[it] < GameConfig.MAX_DECOY_PER_FAMILY }
            if (open.isEmpty()) break
            val pick = open[random.nextInt(open.size)]
            counts[pick] = counts[pick] + 1
            left--
        }
        return counts
    }
}
