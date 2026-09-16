package com.habitmood.tracker.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class HabitRepository(
    private val habitDao: HabitDao,
    private val moodDao: MoodDao
) {

    fun activeHabits(): Flow<List<Habit>> = habitDao.getActiveHabits()

    fun allHabits(): Flow<List<Habit>> = habitDao.getAllHabits()

    fun completionsForDay(date: LocalDate): Flow<List<HabitCompletion>> =
        habitDao.getCompletionsForDay(date.toEpochDay())

    fun completionsBetween(start: LocalDate, end: LocalDate): Flow<List<HabitCompletion>> =
        habitDao.getCompletionsBetween(start.toEpochDay(), end.toEpochDay())

    fun completionsForHabit(habitId: Long): Flow<List<HabitCompletion>> =
        habitDao.getCompletionsForHabit(habitId)

    fun moodForDay(date: LocalDate): Flow<MoodEntry?> =
        moodDao.getMoodForDay(date.toEpochDay())

    fun moodsBetween(start: LocalDate, end: LocalDate): Flow<List<MoodEntry>> =
        moodDao.getMoodsBetween(start.toEpochDay(), end.toEpochDay())

    suspend fun addHabit(name: String, emoji: String, colorHex: String, targetDays: Int, sortOrder: Int) {
        habitDao.insertHabit(
            Habit(
                name = name,
                emoji = emoji,
                colorHex = colorHex,
                targetDaysPerWeek = targetDays,
                createdAtEpochDay = LocalDate.now().toEpochDay(),
                sortOrder = sortOrder
            )
        )
    }

    suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)

    suspend fun archiveHabit(habitId: Long) = habitDao.archiveHabit(habitId)

    suspend fun deleteHabit(habit: Habit) = habitDao.deleteHabit(habit)

    suspend fun toggleCompletion(habitId: Long, date: LocalDate) {
        val day = date.toEpochDay()
        val existing = habitDao.getCompletion(habitId, day)
        if (existing != null && existing.completed) {
            habitDao.deleteCompletion(habitId, day)
        } else {
            habitDao.upsertCompletion(
                HabitCompletion(habitId = habitId, dateEpochDay = day, completed = true)
            )
        }
    }

    suspend fun setMood(date: LocalDate, score: Int, note: String = "") {
        moodDao.upsertMood(MoodEntry(dateEpochDay = date.toEpochDay(), moodScore = score, note = note))
    }

    /**
     * Consecutive-day streak ending today. If today isn't logged yet, we count
     * from yesterday backwards so an in-progress day doesn't zero out a real streak.
     */
    suspend fun currentStreak(habitId: Long): Int {
        var streak = 0
        var day = LocalDate.now()

        if (habitDao.getCompletion(habitId, day.toEpochDay())?.completed != true) {
            day = day.minusDays(1)
        }

        while (true) {
            val completion = habitDao.getCompletion(habitId, day.toEpochDay())
            if (completion?.completed == true) {
                streak++
                day = day.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }
}
