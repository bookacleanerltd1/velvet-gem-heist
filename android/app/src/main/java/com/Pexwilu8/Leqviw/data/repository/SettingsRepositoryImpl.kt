package com.Pexwilu8.Leqviw.data.repository

import com.Pexwilu8.Leqviw.data.local.PreferencesStore
import com.Pexwilu8.Leqviw.domain.model.GameSettings
import com.Pexwilu8.Leqviw.domain.model.RevealSpeed
import com.Pexwilu8.Leqviw.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val store: PreferencesStore) : SettingsRepository {

    override fun load(): GameSettings {
        val stored = store.text(KEY_SPEED, RevealSpeed.NORMAL.name)
        val speed = RevealSpeed.entries.firstOrNull { it.name == stored } ?: RevealSpeed.NORMAL
        return GameSettings(
            revealSpeed = speed,
            chalkMarks = store.bool(KEY_CHALK, true),
            hintPlate = store.bool(KEY_HINT, true),
            reducedMotion = store.bool(KEY_MOTION, false)
        )
    }

    override fun save(settings: GameSettings) {
        store.write {
            putString(KEY_SPEED, settings.revealSpeed.name)
            putBoolean(KEY_CHALK, settings.chalkMarks)
            putBoolean(KEY_HINT, settings.hintPlate)
            putBoolean(KEY_MOTION, settings.reducedMotion)
        }
    }

    private companion object {
        const val KEY_SPEED = "reveal_speed"
        const val KEY_CHALK = "chalk_marks"
        const val KEY_HINT = "hint_plate"
        const val KEY_MOTION = "reduced_motion"
    }
}
