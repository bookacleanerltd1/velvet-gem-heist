package com.Pexwilu8.Leqviw.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.core.theme.GemAmethyst
import com.Pexwilu8.Leqviw.core.theme.GemEmerald
import com.Pexwilu8.Leqviw.core.theme.GemRuby
import com.Pexwilu8.Leqviw.core.theme.GemSapphire
import com.Pexwilu8.Leqviw.core.theme.GemTopaz
import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.model.GemCut
import com.Pexwilu8.Leqviw.domain.model.MissionKind
import com.Pexwilu8.Leqviw.domain.model.RewardKind
import com.Pexwilu8.Leqviw.domain.model.RevealSpeed
import com.Pexwilu8.Leqviw.domain.model.StageWing

object GemVisuals {

    @DrawableRes
    fun sprite(gem: Gem): Int = when (gem) {
        Gem.RUBY -> R.drawable.sprite_1
        Gem.EMERALD -> R.drawable.sprite_2
        Gem.SAPPHIRE -> R.drawable.sprite_3
        Gem.TOPAZ -> R.drawable.sprite_4
        Gem.AMETHYST -> R.drawable.sprite_5
    }

    fun tint(gem: Gem): Color = when (gem) {
        Gem.RUBY -> GemRuby
        Gem.EMERALD -> GemEmerald
        Gem.SAPPHIRE -> GemSapphire
        Gem.TOPAZ -> GemTopaz
        Gem.AMETHYST -> GemAmethyst
    }

    @StringRes
    fun nameRes(gem: Gem): Int = when (gem) {
        Gem.RUBY -> R.string.gem_ruby
        Gem.EMERALD -> R.string.gem_emerald
        Gem.SAPPHIRE -> R.string.gem_sapphire
        Gem.TOPAZ -> R.string.gem_topaz
        Gem.AMETHYST -> R.string.gem_amethyst
    }

    @StringRes
    fun cutRes(cut: GemCut): Int = when (cut) {
        GemCut.ROUGH -> R.string.cut_rough
        GemCut.CUT -> R.string.cut_cut
        GemCut.FLAWLESS -> R.string.cut_flawless
    }

    @StringRes
    fun wingRes(wing: StageWing): Int = when (wing) {
        StageWing.VELVET_LOUNGE -> R.string.wing_lounge
        StageWing.GILDED_VAULT -> R.string.wing_vault
        StageWing.MIDNIGHT_TERRACE -> R.string.wing_terrace
    }

    @StringRes
    fun rewardRes(kind: RewardKind): Int = when (kind) {
        RewardKind.BRASS_CASE -> R.string.reward_brass
        RewardKind.VELVET_POUCH -> R.string.reward_pouch
        RewardKind.GILDED_CASKET -> R.string.reward_casket
    }

    @StringRes
    fun rewardGoalRes(kind: RewardKind): Int = when (kind) {
        RewardKind.BRASS_CASE -> R.string.reward_brass_goal
        RewardKind.VELVET_POUCH -> R.string.reward_pouch_goal
        RewardKind.GILDED_CASKET -> R.string.reward_casket_goal
    }

    @DrawableRes
    fun rewardIcon(kind: RewardKind): Int = when (kind) {
        RewardKind.BRASS_CASE -> R.drawable.sprite_4
        RewardKind.VELVET_POUCH -> R.drawable.sprite_5
        RewardKind.GILDED_CASKET -> R.drawable.sprite_1
    }

    @StringRes
    fun missionRes(kind: MissionKind): Int = when (kind) {
        MissionKind.LIFT_STONES -> R.string.mission_lift
        MissionKind.CLEAN_ROOM -> R.string.mission_clean
        MissionKind.CHAIN_RUN -> R.string.mission_chain
    }

    fun missionAccent(kind: MissionKind): Color = when (kind) {
        MissionKind.LIFT_STONES -> GemEmerald
        MissionKind.CLEAN_ROOM -> GemSapphire
        MissionKind.CHAIN_RUN -> GemAmethyst
    }

    @StringRes
    fun speedRes(speed: RevealSpeed): Int = when (speed) {
        RevealSpeed.SLOW -> R.string.speed_slow
        RevealSpeed.NORMAL -> R.string.speed_normal
        RevealSpeed.FAST -> R.string.speed_fast
    }

    fun groupedNumber(value: Int): String {
        val digits = value.toString()
        if (digits.length <= 4) return digits
        val builder = StringBuilder()
        digits.forEachIndexed { index, char ->
            if (index > 0 && (digits.length - index) % 3 == 0) builder.append(' ')
            builder.append(char)
        }
        return builder.toString()
    }
}
