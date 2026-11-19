package com.Pexwilu8.Leqviw.domain.usecase

import com.Pexwilu8.Leqviw.domain.repository.CatalogRepository
import com.Pexwilu8.Leqviw.domain.model.Mission
import com.Pexwilu8.Leqviw.domain.model.MissionKind
import com.Pexwilu8.Leqviw.domain.model.PlayerProgress
import kotlin.math.max

class UpdateMissionsUseCase(private val catalog: CatalogRepository) {

    fun missions(progress: PlayerProgress): List<Mission> =
        MissionKind.entries.mapIndexed { index, kind ->
            val tier = progress.missionTier.getOrElse(index) { 0 }
            Mission(
                kind = kind,
                tier = tier,
                target = catalog.missionTarget(kind, tier),
                progress = progress.missionProgress.getOrElse(index) { 0 },
                reward = catalog.missionReward(kind, tier)
            )
        }

    fun afterCase(
        progress: PlayerProgress,
        liftedDelta: Int,
        cleanRoomsDelta: Int,
        bestChainInRun: Int
    ): List<Int> {
        val current = progress.missionProgress
        return listOf(
            current.getOrElse(0) { 0 } + liftedDelta,
            current.getOrElse(1) { 0 } + cleanRoomsDelta,
            max(current.getOrElse(2) { 0 }, bestChainInRun)
        )
    }

    fun claim(progress: PlayerProgress, index: Int): PlayerProgress {
        val all = missions(progress)
        val mission = all.getOrNull(index) ?: return progress
        if (!mission.complete) return progress
        val tiers = progress.missionTier.toMutableList()
        val values = progress.missionProgress.toMutableList()
        tiers[index] = mission.tier + 1
        values[index] = if (mission.kind == MissionKind.CHAIN_RUN) 0 else max(0, mission.progress - mission.target)
        return progress.copy(
            missionTier = tiers,
            missionProgress = values,
            gems = progress.gems + mission.reward
        )
    }
}
