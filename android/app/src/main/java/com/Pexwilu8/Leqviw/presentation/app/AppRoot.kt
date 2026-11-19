package com.Pexwilu8.Leqviw.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pexwilu8.Leqviw.core.di.AppViewModelFactory
import com.Pexwilu8.Leqviw.core.navigation.Screen
import com.Pexwilu8.Leqviw.core.theme.BackgroundDeep
import com.Pexwilu8.Leqviw.presentation.collection.CollectionScreen
import com.Pexwilu8.Leqviw.presentation.game.GameScreen
import com.Pexwilu8.Leqviw.presentation.gameover.GameOverScreen
import com.Pexwilu8.Leqviw.presentation.levelmap.LevelMapScreen
import com.Pexwilu8.Leqviw.presentation.menu.MenuScreen
import com.Pexwilu8.Leqviw.presentation.missions.MissionsScreen
import com.Pexwilu8.Leqviw.presentation.rewards.RewardsScreen
import com.Pexwilu8.Leqviw.presentation.settings.SettingsScreen
import com.Pexwilu8.Leqviw.presentation.splash.SplashScreen
import com.Pexwilu8.Leqviw.presentation.tutorial.TutorialScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                if (targetState is Screen.Game) {
                    (slideInHorizontally(tween(300)) { it / 4 } + fadeIn(tween(300)))
                        .togetherWith(fadeOut(tween(220)))
                } else {
                    fadeIn(tween(380)) togetherWith fadeOut(tween(260))
                }
            },
            label = "screenHost"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashScreen(onFinished = appViewModel::onSplashFinished)

                Screen.Menu -> MenuScreen(
                    onPlay = { appViewModel.startCase(it) },
                    onOpen = appViewModel::navigate
                )

                is Screen.Game -> GameScreen(
                    stageIndex = target.stageIndex,
                    session = target.session,
                    onFinished = appViewModel::showResult,
                    onMenu = appViewModel::toMenu
                )

                is Screen.GameOver -> GameOverScreen(
                    result = target.result,
                    onPlayAgain = { appViewModel.startCase(target.result.stageIndex) },
                    onLevelMap = { appViewModel.navigate(Screen.LevelMap) },
                    onRewards = { appViewModel.navigate(Screen.Rewards) },
                    onMenu = appViewModel::toMenu
                )

                Screen.LevelMap -> LevelMapScreen(
                    onPlay = { appViewModel.startCase(it) },
                    onMenu = appViewModel::toMenu
                )

                Screen.Tutorial -> TutorialScreen(
                    onStart = { appViewModel.startCase(0) },
                    onMenu = appViewModel::toMenu
                )

                Screen.Rewards -> RewardsScreen(onMenu = appViewModel::toMenu)

                Screen.Collection -> CollectionScreen(onMenu = appViewModel::toMenu)

                Screen.Missions -> MissionsScreen(onMenu = appViewModel::toMenu)

                Screen.Settings -> SettingsScreen(onMenu = appViewModel::toMenu)
            }
        }
    }
}
