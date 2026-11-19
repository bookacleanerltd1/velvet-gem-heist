package com.Pexwilu8.Leqviw.domain.repository

import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.model.MissionKind
import com.Pexwilu8.Leqviw.domain.model.RewardPlan
import com.Pexwilu8.Leqviw.domain.model.Stage
import com.Pexwilu8.Leqviw.domain.model.StageWing

interface CatalogRepository {
    fun stages(): List<Stage>
    fun rewardPlans(): List<RewardPlan>
    fun missionTarget(kind: MissionKind, tier: Int): Int
    fun missionReward(kind: MissionKind, tier: Int): Int
    fun homeWing(gem: Gem): StageWing
}
