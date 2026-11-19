package com.Pexwilu8.Leqviw.domain.usecase

import com.Pexwilu8.Leqviw.core.config.GameConfig
import kotlin.math.min
import kotlin.math.roundToInt

class ScoreLiftUseCase {

    fun multiplier(chain: Int): Float =
        min(1f + GameConfig.CHAIN_STEP * chain, GameConfig.MAX_CHAIN_MULTIPLIER)

    fun points(chain: Int, roomMultiplier: Int): Int =
        (GameConfig.BASE_MATCH_SCORE * multiplier(chain) * roomMultiplier).roundToInt()

    fun stars(score: Int): Int = when {
        score >= GameConfig.THREE_STAR_SCORE -> 3
        score >= GameConfig.TWO_STAR_SCORE -> 2
        else -> 1
    }
}
