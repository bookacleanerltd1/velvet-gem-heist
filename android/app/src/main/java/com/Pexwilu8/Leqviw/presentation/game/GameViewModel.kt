package com.Pexwilu8.Leqviw.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pexwilu8.Leqviw.core.config.GameConfig
import com.Pexwilu8.Leqviw.domain.model.BranchDoor
import com.Pexwilu8.Leqviw.domain.model.CaseResult
import com.Pexwilu8.Leqviw.domain.model.Gem
import com.Pexwilu8.Leqviw.domain.repository.CatalogRepository
import com.Pexwilu8.Leqviw.domain.repository.ProgressRepository
import com.Pexwilu8.Leqviw.domain.repository.SettingsRepository
import com.Pexwilu8.Leqviw.domain.usecase.GenerateBoardUseCase
import com.Pexwilu8.Leqviw.domain.usecase.ScoreLiftUseCase
import com.Pexwilu8.Leqviw.domain.usecase.UnlockRewardsUseCase
import com.Pexwilu8.Leqviw.domain.usecase.UpdateMissionsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val catalog: CatalogRepository,
    private val generateBoard: GenerateBoardUseCase,
    private val scoreLift: ScoreLiftUseCase,
    private val updateMissions: UpdateMissionsUseCase,
    private val unlockRewards: UnlockRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val resultChannel = Channel<CaseResult>(Channel.BUFFERED)
    val results: Flow<CaseResult> = resultChannel.receiveAsFlow()

    private var activeSession = Int.MIN_VALUE
    private var sweepJob: Job? = null
    private var revealJob: Job? = null
    private var clueJob: Job? = null
    private var hintJob: Job? = null

    private var revealMs = GameConfig.SWEEP_MIN_MS
    private var attempt = 0
    private var cleanRooms = 0
    private var roomHadSuspicion = false
    private var liftedByGem = IntArray(Gem.entries.size)
    private var finished = false
    private val blindRandom = Random(GameConfig.SEED_ATTEMPT_PRIME)

    fun startCase(stageIndex: Int, session: Int) {
        if (activeSession == session) return
        activeSession = session
        prepare(stageIndex)
    }

    fun restartCase() {
        prepare(_state.value.stageIndex)
    }

    private fun prepare(stageIndex: Int) {
        cancelJobs()
        val settings = settingsRepository.load()
        revealMs = settings.revealSpeed.millis
        val stages = catalog.stages()
        val stage = stages.getOrElse(stageIndex) { stages.first() }
        attempt = 0
        cleanRooms = 0
        finished = false
        liftedByGem = IntArray(Gem.entries.size)
        _state.value = GameUiState(
            stageIndex = stage.index,
            wing = stage.wing,
            room = 1,
            lockpicks = stage.startingLockpicks,
            sweepMs = stage.sweepMs,
            chalkEnabled = settings.chalkMarks,
            hintEnabled = settings.hintPlate,
            reducedMotion = settings.reducedMotion
        )
        openRoom()
    }

    private fun openRoom() {
        attempt += 1
        val seed = (_state.value.stageIndex + 1L) * GameConfig.SEED_STAGE_PRIME xor
            (attempt.toLong() * GameConfig.SEED_ATTEMPT_PRIME)
        val layout = generateBoard(seed)
        roomHadSuspicion = false
        _state.update { current ->
            current.copy(
                plates = layout.gems.mapIndexed { index, gem -> PlateView(index = index, gem = gem) },
                clueGems = layout.clueGems,
                clueIndex = 0,
                clueFound = 0,
                suspicion = 0,
                chain = 0,
                chainMultiplier = 1f,
                phase = GamePhase.SWEEP,
                showBranch = false,
                hintIndex = -1,
                seed = seed
            )
        }
        sweepJob = viewModelScope.launch {
            delay(_state.value.sweepMs)
            endSweep()
        }
    }

    private fun endSweep() {
        sweepJob?.cancel()
        sweepJob = null
        if (_state.value.phase != GamePhase.SWEEP) return
        _state.update { it.copy(phase = GamePhase.IDLE) }
        offerHint()
    }

    private fun offerHint() {
        if (!_state.value.hintEnabled) return
        val current = _state.value
        val target = current.plates.firstOrNull { !it.locked && it.gem == current.clueGem } ?: return
        hintJob?.cancel()
        hintJob = viewModelScope.launch {
            _state.update { it.copy(hintIndex = target.index) }
            delay(GameConfig.HINT_MS)
            _state.update { it.copy(hintIndex = -1) }
        }
    }

    fun liftPlate(index: Int) {
        val current = _state.value
        if (current.showPause || current.showExit || current.showBranch) return
        if (current.phase == GamePhase.SWEEP) {
            endSweep()
            return
        }
        if (current.phase != GamePhase.IDLE) return
        val plate = current.plates.getOrNull(index) ?: return
        if (plate.locked) return
        if (plate.gem == current.clueGem) onMatch(index, free = false) else onMiss(index)
    }

    fun goBlind() {
        val current = _state.value
        if (current.showPause || current.showExit || current.showBranch) return
        if (current.phase == GamePhase.SWEEP) {
            endSweep()
            return
        }
        if (current.phase != GamePhase.IDLE) return
        val hidden = current.plates.filter { !it.locked }
        if (hidden.isEmpty()) return
        val picked = hidden[blindRandom.nextInt(hidden.size)]
        if (picked.gem == current.clueGem) onMatch(picked.index, free = false) else onMiss(picked.index)
    }

    fun useLockpick() {
        val current = _state.value
        if (current.phase != GamePhase.IDLE || current.lockpicks <= 0) return
        val target = current.plates.firstOrNull { !it.locked && it.gem == current.clueGem } ?: return
        _state.update { it.copy(lockpicks = it.lockpicks - 1) }
        onMatch(target.index, free = true)
    }

    fun openBranch() {
        if (_state.value.phase != GamePhase.ROOM_CLEARED) return
        _state.update { it.copy(showBranch = true) }
    }

    fun chooseDoor(door: BranchDoor) {
        val current = _state.value
        val roomMultiplier = if (door == BranchDoor.VELVET_CURTAIN) 2 else 1
        val limit = if (door == BranchDoor.VELVET_CURTAIN) {
            GameConfig.TIGHT_SUSPICION
        } else {
            GameConfig.MAX_SUSPICION
        }
        val picks = if (door == BranchDoor.SERVICE_HALL) {
            min(current.lockpicks + 1, GameConfig.MAX_LOCKPICKS)
        } else {
            current.lockpicks
        }
        _state.update {
            it.copy(
                room = it.room + 1,
                roomMultiplier = roomMultiplier,
                suspicionLimit = limit,
                lockpicks = picks,
                showBranch = false
            )
        }
        openRoom()
    }

    fun openPause() {
        if (_state.value.phase == GamePhase.SWEEP) endSweep()
        _state.update { it.copy(showPause = true) }
    }

    fun closePause() {
        _state.update { it.copy(showPause = false) }
    }

    fun requestExit() {
        if (_state.value.phase == GamePhase.SWEEP) endSweep()
        _state.update { it.copy(showExit = true, showPause = false) }
    }

    fun dismissExit() {
        _state.update { it.copy(showExit = false) }
    }

    fun pauseRound() {
        cancelJobs()
        _state.update { s ->
            when (s.phase) {
                GamePhase.SWEEP, GamePhase.REVEALING -> s.copy(
                    phase = GamePhase.IDLE,
                    plates = s.plates.map { if (it.locked) it else it.copy(revealed = false) }
                )

                GamePhase.CLUE_SOLVED -> s.copy(
                    phase = GamePhase.IDLE,
                    clueIndex = min(s.clueIndex + 1, s.clueGems.lastIndex),
                    clueFound = 0,
                    plates = s.plates.map {
                        if (it.locked) it else it.copy(revealed = false, marked = false)
                    }
                )

                else -> s
            }
        }
    }

    private fun onMatch(index: Int, free: Boolean) {
        val current = _state.value
        val plate = current.plates.getOrNull(index) ?: return
        val chain = if (free) 0 else current.chain + 1
        val points = if (free) {
            GameConfig.BASE_MATCH_SCORE * current.roomMultiplier
        } else {
            scoreLift.points(chain, current.roomMultiplier)
        }
        var picks = current.lockpicks
        if (!free && chain > 0 && chain % GameConfig.LOCKPICK_CHAIN_STEP == 0) {
            picks = min(picks + 1, GameConfig.MAX_LOCKPICKS)
        }
        liftedByGem[plate.gem.ordinal] = liftedByGem[plate.gem.ordinal] + 1
        val found = current.clueFound + 1
        _state.update { s ->
            s.copy(
                plates = s.plates.map {
                    if (it.index == index) it.copy(revealed = true, locked = true, marked = false) else it
                },
                chain = chain,
                chainMultiplier = scoreLift.multiplier(chain),
                bestChain = max(s.bestChain, chain),
                score = s.score + points,
                gemsLifted = s.gemsLifted + 1,
                lockpicks = picks,
                clueFound = found,
                hintIndex = -1
            )
        }
        if (found >= GameConfig.PLATES_PER_CLUE) advanceClue()
    }

    private fun advanceClue() {
        val current = _state.value
        val next = current.clueIndex + 1
        if (next >= current.clueGems.size) {
            clearRoom()
            return
        }
        _state.update { it.copy(phase = GamePhase.CLUE_SOLVED) }
        clueJob?.cancel()
        clueJob = viewModelScope.launch {
            delay(GameConfig.CLUE_SWAP_MS)
            _state.update { s ->
                s.copy(
                    clueIndex = next,
                    clueFound = 0,
                    plates = s.plates.map {
                        if (it.locked) it else it.copy(revealed = false, marked = false)
                    },
                    phase = GamePhase.IDLE
                )
            }
            offerHint()
        }
    }

    private fun clearRoom() {
        if (!roomHadSuspicion) cleanRooms += 1
        val lastRoom = _state.value.room >= GameConfig.ROOMS_PER_CASE
        _state.update {
            it.copy(
                phase = GamePhase.ROOM_CLEARED,
                score = it.score + GameConfig.ROOM_BONUS,
                gemsEarned = it.gemsEarned + GameConfig.GEMS_PER_ROOM,
                hintIndex = -1
            )
        }
        if (lastRoom) finish(caseClosed = true)
    }

    private fun onMiss(index: Int) {
        revealJob?.cancel()
        hintJob?.cancel()
        _state.update { s ->
            s.copy(
                phase = GamePhase.REVEALING,
                plates = s.plates.map { if (it.index == index) it.copy(revealed = true) else it },
                hintIndex = -1
            )
        }
        revealJob = viewModelScope.launch {
            delay(revealMs)
            val chalk = _state.value.chalkEnabled
            _state.update { s ->
                s.copy(
                    plates = s.plates.map {
                        if (it.index == index) it.copy(revealed = false, marked = chalk) else it
                    },
                    chain = 0,
                    chainMultiplier = 1f,
                    suspicion = s.suspicion + 1
                )
            }
            roomHadSuspicion = true
            delay(GameConfig.FLIP_MS)
            if (_state.value.suspicion >= _state.value.suspicionLimit) {
                _state.update { it.copy(phase = GamePhase.ALARM) }
                delay(GameConfig.ALARM_MS)
                finish(caseClosed = false)
            } else {
                _state.update { it.copy(phase = GamePhase.IDLE) }
            }
        }
    }

    private fun finish(caseClosed: Boolean) {
        if (finished) return
        finished = true
        cancelJobs()
        val current = _state.value
        val progress = progressRepository.load()
        val stageIndex = current.stageIndex
        val stars = if (caseClosed) scoreLift.stars(current.score) else 0
        val roomsCleared = if (caseClosed) GameConfig.ROOMS_PER_CASE else current.room - 1
        val newRecord = current.score > progress.bestScore
        val earnedGems = current.gemsLifted + current.gemsEarned +
            if (caseClosed) GameConfig.GEMS_PER_CASE else 0

        val starsList = progress.stageStars.toMutableList()
        val bestList = progress.stageBest.toMutableList()
        if (stageIndex in starsList.indices) {
            starsList[stageIndex] = max(starsList[stageIndex], stars)
            bestList[stageIndex] = max(bestList[stageIndex], current.score)
        }
        val unlocked = if (caseClosed) {
            max(progress.unlockedStages, min(stageIndex + 2, GameConfig.STAGE_COUNT))
        } else {
            progress.unlockedStages
        }
        val updated = progress.copy(
            bestScore = max(progress.bestScore, current.score),
            gems = progress.gems + earnedGems,
            unlockedStages = unlocked,
            stageStars = starsList,
            stageBest = bestList,
            gemLifted = progress.gemLifted.mapIndexed { index, value ->
                value + liftedByGem.getOrElse(index) { 0 }
            },
            totalLifted = progress.totalLifted + current.gemsLifted,
            casesClosed = progress.casesClosed + if (caseClosed) 1 else 0,
            bestChain = max(progress.bestChain, current.bestChain),
            cleanRooms = progress.cleanRooms + cleanRooms,
            missionProgress = updateMissions.afterCase(
                progress = progress,
                liftedDelta = current.gemsLifted,
                cleanRoomsDelta = cleanRooms,
                bestChainInRun = current.bestChain
            )
        )
        progressRepository.save(updated)
        _state.update {
            it.copy(phase = if (caseClosed) GamePhase.CASE_CLOSED else GamePhase.ALARM)
        }
        val result = CaseResult(
            stageIndex = stageIndex,
            wing = current.wing,
            score = current.score,
            gemsLifted = current.gemsLifted,
            bestChain = current.bestChain,
            roomsCleared = roomsCleared,
            roomsPerCase = GameConfig.ROOMS_PER_CASE,
            caseClosed = caseClosed,
            newRecord = newRecord,
            stars = stars,
            rewardsWaiting = unlockRewards.waiting(updated)
        )
        viewModelScope.launch { resultChannel.send(result) }
    }

    private fun cancelJobs() {
        sweepJob?.cancel()
        revealJob?.cancel()
        clueJob?.cancel()
        hintJob?.cancel()
        sweepJob = null
        revealJob = null
        clueJob = null
        hintJob = null
    }

    override fun onCleared() {
        cancelJobs()
        super.onCleared()
    }
}
