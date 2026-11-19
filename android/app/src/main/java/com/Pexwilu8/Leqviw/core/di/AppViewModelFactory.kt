package com.Pexwilu8.Leqviw.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Pexwilu8.Leqviw.presentation.app.AppViewModel
import com.Pexwilu8.Leqviw.presentation.collection.CollectionViewModel
import com.Pexwilu8.Leqviw.presentation.game.GameViewModel
import com.Pexwilu8.Leqviw.presentation.gameover.GameOverViewModel
import com.Pexwilu8.Leqviw.presentation.levelmap.LevelMapViewModel
import com.Pexwilu8.Leqviw.presentation.menu.MenuViewModel
import com.Pexwilu8.Leqviw.presentation.missions.MissionsViewModel
import com.Pexwilu8.Leqviw.presentation.rewards.RewardsViewModel
import com.Pexwilu8.Leqviw.presentation.settings.SettingsViewModel
import com.Pexwilu8.Leqviw.presentation.splash.SplashViewModel
import com.Pexwilu8.Leqviw.presentation.tutorial.TutorialViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val progress = ServiceLocator.progressRepository()
        val settings = ServiceLocator.settingsRepository()
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(progress, ServiceLocator.unlockRewards)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    progressRepository = progress,
                    settingsRepository = settings,
                    catalog = ServiceLocator.catalog,
                    generateBoard = ServiceLocator.generateBoard,
                    scoreLift = ServiceLocator.scoreLift,
                    updateMissions = ServiceLocator.updateMissions,
                    unlockRewards = ServiceLocator.unlockRewards
                )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(progress)

            modelClass.isAssignableFrom(LevelMapViewModel::class.java) ->
                LevelMapViewModel(progress, ServiceLocator.catalog)

            modelClass.isAssignableFrom(TutorialViewModel::class.java) ->
                TutorialViewModel(progress)

            modelClass.isAssignableFrom(RewardsViewModel::class.java) ->
                RewardsViewModel(progress, ServiceLocator.unlockRewards)

            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(progress, ServiceLocator.buildCollection)

            modelClass.isAssignableFrom(MissionsViewModel::class.java) ->
                MissionsViewModel(progress, ServiceLocator.updateMissions)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settings, progress)

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
