package com.Pexwilu8.Leqviw.domain.usecase

import com.Pexwilu8.Leqviw.domain.repository.CatalogRepository
import com.Pexwilu8.Leqviw.domain.model.PlayerProgress
import com.Pexwilu8.Leqviw.domain.model.Reward
import com.Pexwilu8.Leqviw.domain.model.RewardKind

class UnlockRewardsUseCase(private val catalog: CatalogRepository) {

    fun rewards(progress: PlayerProgress): List<Reward> =
        catalog.rewardPlans().map { plan ->
            Reward(
                kind = plan.kind,
                goal = plan.goal,
                progress = progressFor(plan.kind, progress),
                gems = plan.gems,
                claimed = progress.claimedRewards.contains(plan.kind.name)
            )
        }

    fun waiting(progress: PlayerProgress): Int =
        rewards(progress).count { it.earned && !it.claimed }

    fun claim(progress: PlayerProgress, kind: RewardKind): PlayerProgress {
        val reward = rewards(progress).firstOrNull { it.kind == kind } ?: return progress
        if (!reward.earned || reward.claimed) return progress
        return progress.copy(
            gems = progress.gems + reward.gems,
            claimedRewards = progress.claimedRewards + kind.name
        )
    }

    private fun progressFor(kind: RewardKind, progress: PlayerProgress): Int = when (kind) {
        RewardKind.BRASS_CASE -> progress.bestScore
        RewardKind.VELVET_POUCH -> progress.totalLifted
        RewardKind.GILDED_CASKET -> progress.casesClosed
    }
}
