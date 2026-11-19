package com.Pexwilu8.Leqviw.presentation.tutorial

data class TutorialStep(
    val titleRes: Int,
    val bodyRes: Int,
    val spriteRes: Int
)

data class TutorialUiState(
    val steps: List<TutorialStep> = emptyList(),
    val index: Int = 0
) {
    val step: TutorialStep? get() = steps.getOrNull(index)
    val isLast: Boolean get() = index >= steps.lastIndex
}
