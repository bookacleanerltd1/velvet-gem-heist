package com.Pexwilu8.Leqviw.core.di

import android.content.Context
import com.Pexwilu8.Leqviw.data.local.PreferencesStore
import com.Pexwilu8.Leqviw.data.repository.ProgressRepositoryImpl
import com.Pexwilu8.Leqviw.data.repository.SettingsRepositoryImpl
import com.Pexwilu8.Leqviw.data.sample.SampleData
import com.Pexwilu8.Leqviw.domain.repository.CatalogRepository
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import com.Pexwilu8.Leqviw.domain.repository.SettingsRepository
import com.Pexwilu8.Leqviw.domain.usecase.BuildCollectionUseCase
import com.Pexwilu8.Leqviw.domain.usecase.GenerateBoardUseCase
import com.Pexwilu8.Leqviw.domain.usecase.ScoreLiftUseCase
import com.Pexwilu8.Leqviw.domain.usecase.UnlockRewardsUseCase
import com.Pexwilu8.Leqviw.domain.usecase.UpdateMissionsUseCase

object ServiceLocator {

    private var store: PreferencesStore? = null
    private var progress: ProgressRepository? = null
    private var settings: SettingsRepository? = null

    val catalog: CatalogRepository = SampleData
    val generateBoard: GenerateBoardUseCase = GenerateBoardUseCase()
    val scoreLift: ScoreLiftUseCase = ScoreLiftUseCase()
    val updateMissions: UpdateMissionsUseCase = UpdateMissionsUseCase(SampleData)
    val unlockRewards: UnlockRewardsUseCase = UnlockRewardsUseCase(SampleData)
    val buildCollection: BuildCollectionUseCase = BuildCollectionUseCase(SampleData)

    fun init(context: Context) {
        if (store != null) return
        val created = PreferencesStore(context.applicationContext)
        store = created
        progress = ProgressRepositoryImpl(created)
        settings = SettingsRepositoryImpl(created)
    }

    fun progressRepository(): ProgressRepository =
        progress ?: error(NOT_READY)

    fun settingsRepository(): SettingsRepository =
        settings ?: error(NOT_READY)

    private const val NOT_READY = "ServiceLocator.init must run before the first ViewModel"
}
