package com.Pexwilu8.Leqviw.presentation.gameover

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        _state.update {
            it.copy(
                bestScore = progress.bestScore,
                totalGems = progress.gems,
                casesClosed = progress.casesClosed
            )
        }
    }
}
