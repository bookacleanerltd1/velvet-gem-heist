package com.Pexwilu8.Leqviw.presentation.missions

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import com.Pexwilu8.Leqviw.domain.usecase.UpdateMissionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MissionsViewModel(
    private val progressRepository: ProgressRepository,
    private val updateMissions: UpdateMissionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MissionsUiState())
    val state: StateFlow<MissionsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        _state.update {
            it.copy(missions = updateMissions.missions(progress), gems = progress.gems)
        }
    }

    fun claim(index: Int) {
        val progress = progressRepository.load()
        val updated = updateMissions.claim(progress, index)
        if (updated == progress) return
        progressRepository.save(updated)
        refresh()
    }
}
