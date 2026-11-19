package com.Pexwilu8.Leqviw.presentation.tutorial

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.R
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TutorialViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        TutorialUiState(
            steps = listOf(
                TutorialStep(R.string.tutorial_title_1, R.string.tutorial_body_1, R.drawable.sprite_2),
                TutorialStep(R.string.tutorial_title_2, R.string.tutorial_body_2, R.drawable.sprite_1),
                TutorialStep(R.string.tutorial_title_3, R.string.tutorial_body_3, R.drawable.sprite_3),
                TutorialStep(R.string.tutorial_title_4, R.string.tutorial_body_4, R.drawable.sprite_5)
            )
        )
    )
    val state: StateFlow<TutorialUiState> = _state.asStateFlow()

    fun next() {
        _state.update { it.copy(index = (it.index + 1).coerceAtMost(it.steps.lastIndex)) }
    }

    fun previous() {
        _state.update { it.copy(index = (it.index - 1).coerceAtLeast(0)) }
    }

    fun markSeen() {
        val progress = progressRepository.load()
        if (progress.tutorialSeen) return
        progressRepository.save(progress.copy(tutorialSeen = true))
    }
}
