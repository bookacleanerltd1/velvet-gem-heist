package com.Pexwilu8.Leqviw.presentation.collection

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import com.Pexwilu8.Leqviw.domain.usecase.BuildCollectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CollectionViewModel(
    private val progressRepository: ProgressRepository,
    private val buildCollection: BuildCollectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.load()
        _state.update { it.copy(entries = buildCollection(progress)) }
    }

    fun select(index: Int) {
        val entry = _state.value.entries.getOrNull(index) ?: return
        if (!entry.discovered) return
        _state.update { it.copy(selected = if (it.selected == index) -1 else index) }
    }
}
