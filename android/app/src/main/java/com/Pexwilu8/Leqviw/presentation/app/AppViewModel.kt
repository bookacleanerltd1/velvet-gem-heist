package com.Pexwilu8.Leqviw.presentation.app

import androidx.lifecycle.ViewModel
import com.Pexwilu8.Leqviw.core.navigation.Screen
import com.Pexwilu8.Leqviw.domain.model.CaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()
    private var sessionCounter = 0

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun onSplashFinished() {
        if (_screen.value != Screen.Splash) return
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun navigate(target: Screen) {
        backStack.add(_screen.value)
        _screen.value = target
        syncBack()
    }

    fun startCase(stageIndex: Int) {
        sessionCounter += 1
        navigate(Screen.Game(stageIndex, sessionCounter))
    }

    fun showResult(result: CaseResult) {
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.GameOver(result)
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBack()
    }

    fun toMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
