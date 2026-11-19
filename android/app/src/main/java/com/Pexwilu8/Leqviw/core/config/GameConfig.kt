package com.Pexwilu8.Leqviw.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val BOARD_COLS = 4
    const val BOARD_ROWS = 4
    const val PLATE_COUNT = 16
    const val CLUES_PER_ROOM = 2
    const val PLATES_PER_CLUE = 3
    const val DECOY_COUNT = 10
    const val MAX_DECOY_PER_FAMILY = 4
    const val GEN_ATTEMPTS = 20

    const val ROOMS_PER_CASE = 3
    const val MAX_SUSPICION = 3
    const val TIGHT_SUSPICION = 2

    const val BASE_MATCH_SCORE = 100
    const val ROOM_BONUS = 250
    const val CASE_BONUS = 750
    const val GEMS_PER_ROOM = 5
    const val GEMS_PER_CASE = 15
    const val CHAIN_STEP = 0.5f
    const val MAX_CHAIN_MULTIPLIER = 3.0f
    const val LOCKPICK_CHAIN_STEP = 3
    const val MAX_LOCKPICKS = 3

    const val SWEEP_BASE_MS = 1700L
    const val SWEEP_STEP_MS = 100L
    const val SWEEP_MIN_MS = 1200L
    const val FLIP_MS = 220L
    const val ALARM_MS = 360L
    const val CLUE_SWAP_MS = 300L
    const val HINT_MS = 1200L

    const val STAGE_COUNT = 9
    const val TWO_STAR_SCORE = 2000
    const val THREE_STAR_SCORE = 3500

    const val COLLECTION_CUT_COUNT = 3
    const val CUT_ROUGH_AT = 1
    const val CUT_CUT_AT = 10
    const val CUT_FLAWLESS_AT = 25

    const val SEED_STAGE_PRIME = 7919L
    const val SEED_ATTEMPT_PRIME = 104729L
}
