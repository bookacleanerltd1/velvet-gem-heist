package com.Pexwilu8.Leqviw.presentation.menu

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import com.Pexwilu8.Leqviw.domain.usecase.UnlockRewardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val unlockRewards: UnlockRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        _state.update {
            it.copy(
                bestScore = progress.bestScore,
                gems = progress.gems,
                currentStage = (progress.unlockedStages - 1).coerceIn(0, GameConfig.STAGE_COUNT - 1),
                rewardsWaiting = unlockRewards.waiting(progress),
                hasProgress = progress.bestScore > 0 && progress.gems > 0
            )
        }
    }
}
