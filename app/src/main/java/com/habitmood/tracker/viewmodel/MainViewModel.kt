package com.habitmood.tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitmood.tracker.data.Habit
import com.habitmood.tracker.data.HabitCompletion
import com.habitmood.tracker.data.HabitRepository
import com.habitmood.tracker.motivation.DailyStats
import com.habitmood.tracker.motivation.MotivationEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitWithState(
    val habit: Habit,
    val completedToday: Boolean,
    val streak: Int
)

data class TodayUiState(
    val date: LocalDate = LocalDate.now(),
    val habits: List<HabitWithState> = emptyList(),
    val mood: Int? = null,
    val motivation: String = "Add a habit to get your first pep talk!",
    val isLoading: Boolean = true
)

class MainViewModel(private val repository: HabitRepository) : ViewModel() {

    private val today = LocalDate.now()

    private val _mood = MutableStateFlow<Int?>(null)
    private val _motivation = MutableStateFlow("Add a habit to get your first pep talk!")
    private val _streaks = MutableStateFlow<Map<Long, Int>>(emptyMap())

    val allHabits: StateFlow<List<Habit>> = repository.activeHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val todayCompletions: StateFlow<List<HabitCompletion>> = repository.completionsForDay(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<TodayUiState> = combine(
        allHabits, todayCompletions, _mood, _motivation, _streaks
    ) { habits, completions, mood, motivation, streaks ->
        val completedIds = completions.filter { it.completed }.map { it.habitId }.toSet()
        val list = habits.map { habit ->
            HabitWithState(
                habit = habit,
                completedToday = habit.id in completedIds,
                streak = streaks[habit.id] ?: 0
            )
        }
        TodayUiState(
            date = today,
            habits = list,
            mood = mood,
            motivation = motivation,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodayUiState())

    init {
        viewModelScope.launch {
            repository.moodForDay(today).collect { entry ->
                _mood.value = entry?.moodScore
                refreshStreaksAndMotivation()
            }
        }
        viewModelScope.launch {
            allHabits.collect { refreshStreaksAndMotivation() }
        }
        viewModelScope.launch {
            todayCompletions.collect { refreshStreaksAndMotivation() }
        }
    }

    private suspend fun refreshStreaksAndMotivation() {
        val habits = allHabits.value
        val completions = todayCompletions.value
        val completedIds = completions.filter { it.completed }.map { it.habitId }.toSet()

        val streakMap = habits.associate { it.id to repository.currentStreak(it.id) }
        _streaks.value = streakMap

        val best = streakMap.maxByOrNull { it.value }
        val bestHabitName = habits.firstOrNull { it.id == best?.key }?.name

        _motivation.value = MotivationEngine.generate(
            DailyStats(
                totalHabits = habits.size,
                completedHabits = completedIds.size,
                bestStreak = best?.value ?: 0,
                bestStreakHabitName = bestHabitName,
                moodScore = _mood.value
            )
        )
    }

    fun toggleHabit(habitId: Long) {
        viewModelScope.launch {
            repository.toggleCompletion(habitId, today)
            refreshStreaksAndMotivation()
        }
    }

    fun addHabit(name: String, emoji: String, colorHex: String, targetDays: Int) {
        viewModelScope.launch {
            repository.addHabit(name, emoji, colorHex, targetDays, sortOrder = allHabits.value.size)
        }
    }

    fun archiveHabit(habitId: Long) {
        viewModelScope.launch { repository.archiveHabit(habitId) }
    }

    fun setMood(score: Int, note: String = "") {
        viewModelScope.launch { repository.setMood(today, score, note) }
    }

    fun regenerateMotivation() {
        viewModelScope.launch { refreshStreaksAndMotivation() }
    }

    fun weeklyCompletionFlow(): Flow<Pair<List<LocalDate>, Map<LocalDate, Int>>> {
        val start = today.minusDays(6)
        return repository.completionsBetween(start, today).map { completions ->
            val days = (0..6).map { start.plusDays(it.toLong()) }
            val counts = completions.filter { it.completed }
                .groupingBy { LocalDate.ofEpochDay(it.dateEpochDay) }
                .eachCount()
            days to counts
        }
    }

    fun weeklyMoodFlow(): Flow<Map<LocalDate, Int>> {
        val start = today.minusDays(6)
        return repository.moodsBetween(start, today).map { moods ->
            moods.associate { LocalDate.ofEpochDay(it.dateEpochDay) to it.moodScore }
        }
    }
}
