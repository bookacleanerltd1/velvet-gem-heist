package com.Pexwilu8.Leqviw.presentation.settings

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.domain.model.GameSettings
import com.Pexwilu8.Leqviw.domain.model.RevealSpeed
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import com.Pexwilu8.Leqviw.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        _state.update {
            it.copy(
                settings = settingsRepository.load(),
                gems = progress.gems,
                casesClosed = progress.casesClosed
            )
        }
    }

    fun setSpeed(speed: RevealSpeed) = persist { it.copy(revealSpeed = speed) }

    fun toggleChalk() = persist { it.copy(chalkMarks = !it.chalkMarks) }

    fun toggleHint() = persist { it.copy(hintPlate = !it.hintPlate) }

    fun toggleMotion() = persist { it.copy(reducedMotion = !it.reducedMotion) }

    fun askReset() {
        _state.update { it.copy(showReset = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(showReset = false) }
    }

    fun confirmReset() {
        progressRepository.reset()
        _state.update { it.copy(showReset = false) }
        refresh()
    }

    private fun persist(transform: (GameSettings) -> GameSettings) {
        val updated = transform(_state.value.settings)
        settingsRepository.save(updated)
        _state.update { it.copy(settings = updated) }
    }
}
