package com.Pexwilu8.Leqviw.data.repository

import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.data.local.PreferencesStore
import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.model.PlayerProgress
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val store: PreferencesStore) : ProgressRepository {

    override fun load(): PlayerProgress = PlayerProgress(
        bestScore = store.int(KEY_BEST, 0),
        gems = store.int(KEY_GEMS, 0),
        unlockedStages = store.int(KEY_UNLOCKED, 1).coerceIn(1, GameConfig.STAGE_COUNT),
        stageStars = store.intList(KEY_STARS, GameConfig.STAGE_COUNT),
        stageBest = store.intList(KEY_STAGE_BEST, GameConfig.STAGE_COUNT),
        gemLifted = store.intList(KEY_GEM_LIFTED, Gem.entries.size),
        totalLifted = store.int(KEY_TOTAL_LIFTED, 0),
        casesClosed = store.int(KEY_CASES, 0),
        bestChain = store.int(KEY_BEST_CHAIN, 0),
        cleanRooms = store.int(KEY_CLEAN_ROOMS, 0),
        missionTier = store.intList(KEY_MISSION_TIER, 3),
        missionProgress = store.intList(KEY_MISSION_PROGRESS, 3),
        claimedRewards = store.textSet(KEY_CLAIMED),
        tutorialSeen = store.bool(KEY_TUTORIAL, false)
    )

    override fun save(progress: PlayerProgress) {
        store.write {
            putInt(KEY_BEST, progress.bestScore)
            putInt(KEY_GEMS, progress.gems)
            putInt(KEY_UNLOCKED, progress.unlockedStages)
            putString(KEY_STARS, PreferencesStore.join(progress.stageStars))
            putString(KEY_STAGE_BEST, PreferencesStore.join(progress.stageBest))
            putString(KEY_GEM_LIFTED, PreferencesStore.join(progress.gemLifted))
            putInt(KEY_TOTAL_LIFTED, progress.totalLifted)
            putInt(KEY_CASES, progress.casesClosed)
            putInt(KEY_BEST_CHAIN, progress.bestChain)
            putInt(KEY_CLEAN_ROOMS, progress.cleanRooms)
            putString(KEY_MISSION_TIER, PreferencesStore.join(progress.missionTier))
            putString(KEY_MISSION_PROGRESS, PreferencesStore.join(progress.missionProgress))
            putString(KEY_CLAIMED, PreferencesStore.join(progress.claimedRewards))
            putBoolean(KEY_TUTORIAL, progress.tutorialSeen)
        }
    }

    override fun reset() {
        val seen = store.bool(KEY_TUTORIAL, false)
        store.clear()
        store.write { putBoolean(KEY_TUTORIAL, seen) }
    }

    private companion object {
        const val KEY_BEST = "best_score"
        const val KEY_GEMS = "gems"
        const val KEY_UNLOCKED = "unlocked_stages"
        const val KEY_STARS = "stage_stars"
        const val KEY_STAGE_BEST = "stage_best"
        const val KEY_GEM_LIFTED = "gem_lifted"
        const val KEY_TOTAL_LIFTED = "total_lifted"
        const val KEY_CASES = "cases_closed"
        const val KEY_BEST_CHAIN = "best_chain"
        const val KEY_CLEAN_ROOMS = "clean_rooms"
        const val KEY_MISSION_TIER = "mission_tier"
        const val KEY_MISSION_PROGRESS = "mission_progress"
        const val KEY_CLAIMED = "claimed_rewards"
        const val KEY_TUTORIAL = "tutorial_seen"
    }
}
