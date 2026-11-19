package com.Pexwilu8.Leqviw.data.sample

import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.model.MissionKind
import com.Pexwilu8.Leqviw.domain.model.RewardKind
import com.Pexwilu8.Leqviw.domain.model.RewardPlan
import com.Pexwilu8.Leqviw.domain.model.Stage
import com.Pexwilu8.Leqviw.domain.model.StageWing
import com.Pexwilu8.Leqviw.domain.repository.CatalogRepository
import kotlin.math.max

object SampleData : CatalogRepository {

    private val velvetHouse: List<Stage> = listOf(
        Stage(0, StageWing.VELVET_LOUNGE, 1700L, 1),
        Stage(1, StageWing.VELVET_LOUNGE, 1640L, 1),
        Stage(2, StageWing.VELVET_LOUNGE, 1580L, 1),
        Stage(3, StageWing.GILDED_VAULT, 1520L, 2),
        Stage(4, StageWing.GILDED_VAULT, 1460L, 2),
        Stage(5, StageWing.GILDED_VAULT, 1400L, 2),
        Stage(6, StageWing.MIDNIGHT_TERRACE, 1340L, 2),
        Stage(7, StageWing.MIDNIGHT_TERRACE, 1280L, 3),
        Stage(8, StageWing.MIDNIGHT_TERRACE, GameConfig.SWEEP_MIN_MS, 3)
    )

    private val crates: List<RewardPlan> = listOf(
        RewardPlan(RewardKind.BRASS_CASE, 1500, 40),
        RewardPlan(RewardKind.VELVET_POUCH, 60, 70),
        RewardPlan(RewardKind.GILDED_CASKET, 3, 120)
    )

    private val stoneHomes: Map<Gem, StageWing> = mapOf(
        Gem.RUBY to StageWing.VELVET_LOUNGE,
        Gem.EMERALD to StageWing.VELVET_LOUNGE,
        Gem.SAPPHIRE to StageWing.GILDED_VAULT,
        Gem.TOPAZ to StageWing.GILDED_VAULT,
        Gem.AMETHYST to StageWing.MIDNIGHT_TERRACE
    )

    override fun stages(): List<Stage> = velvetHouse

    override fun rewardPlans(): List<RewardPlan> = crates

    override fun missionTarget(kind: MissionKind, tier: Int): Int = when (kind) {
        MissionKind.LIFT_STONES -> 15 * (1 shl minOf(tier, 4))
        MissionKind.CLEAN_ROOM -> tier + 1
        MissionKind.CHAIN_RUN -> 5 + tier * 2
    }

    override fun missionReward(kind: MissionKind, tier: Int): Int = when (kind) {
        MissionKind.LIFT_STONES -> 25 + tier * 15
        MissionKind.CLEAN_ROOM -> 35 + tier * 20
        MissionKind.CHAIN_RUN -> 45 + tier * 25
    }

    override fun homeWing(gem: Gem): StageWing = stoneHomes[gem] ?: StageWing.VELVET_LOUNGE

    fun stageAt(index: Int): Stage {
        val safe = max(0, minOf(index, velvetHouse.lastIndex))
        return velvetHouse[safe]
    }
}
