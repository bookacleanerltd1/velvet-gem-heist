package com.Pexwilu8.Leqviw.domain.model

enum class RevealSpeed(val millis: Long) {
    SLOW(700L),
    NORMAL(450L),
    FAST(250L)
}

data class GameSettings(
    val revealSpeed: RevealSpeed = RevealSpeed.NORMAL,
    val chalkMarks: Boolean = true,
    val hintPlate: Boolean = true,
    val reducedMotion: Boolean = false
)
