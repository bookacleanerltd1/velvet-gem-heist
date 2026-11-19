package com.Pexwilu8.Leqviw.presentation.levelmap

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.domain.repository.CatalogRepository
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LevelMapViewModel(
    private val progressRepository: ProgressRepository,
    private val catalog: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelMapUiState())
    val state: StateFlow<LevelMapUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        val current = (progress.unlockedStages - 1).coerceAtLeast(0)
        val nodes = catalog.stages().map { stage ->
            StageNode(
                index = stage.index,
                wing = stage.wing,
                unlocked = stage.index < progress.unlockedStages,
                stars = progress.stageStars.getOrElse(stage.index) { 0 },
                best = progress.stageBest.getOrElse(stage.index) { 0 },
                current = stage.index == current
            )
        }
        _state.update {
            it.copy(
                nodes = nodes,
                selected = current,
                firstRun = progress.unlockedStages <= 1
            )
        }
    }

    fun select(index: Int) {
        val node = _state.value.nodes.getOrNull(index) ?: return
        if (!node.unlocked) return
        _state.update { it.copy(selected = index) }
    }
}
