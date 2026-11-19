package com.Pexwilu8.Leqviw.domain.repository

import com.Pexwilu8.Leqviw.domain.model.PlayerProgress

interface ProgressRepository {
    fun load(): PlayerProgress
    fun save(progress: PlayerProgress)
    fun reset()
}
