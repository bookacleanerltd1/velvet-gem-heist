package com.Pexwilu8.Leqviw.domain.repository

import com.Pexwilu8.Leqviw.domain.model.GameSettings

interface SettingsRepository {
    fun load(): GameSettings
    fun save(settings: GameSettings)
}
