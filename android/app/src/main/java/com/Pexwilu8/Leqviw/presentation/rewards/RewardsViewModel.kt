package com.Pexwilu8.Leqviw.presentation.rewards

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.domain.model.RewardKind
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import com.Pexwilu8.Leqviw.domain.usecase.UnlockRewardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RewardsViewModel(
    private val progressRepository: ProgressRepository,
    private val unlockRewards: UnlockRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        _state.update {
            it.copy(rewards = unlockRewards.rewards(progress), gems = progress.gems)
        }
    }

    fun claim(kind: RewardKind) {
        val progress = progressRepository.load()
        val updated = unlockRewards.claim(progress, kind)
        if (updated == progress) return
        progressRepository.save(updated)
        refresh()
    }
}
