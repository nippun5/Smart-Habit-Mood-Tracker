package com.habitmood.tracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY sortOrder ASC, createdAtEpochDay ASC")
    fun getActiveHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY sortOrder ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("UPDATE habits SET isArchived = 1 WHERE id = :habitId")
    suspend fun archiveHabit(habitId: Long)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit_completions WHERE dateEpochDay = :day")
    fun getCompletionsForDay(day: Long): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY dateEpochDay DESC")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE dateEpochDay BETWEEN :start AND :end")
    fun getCompletionsBetween(start: Long, end: Long): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND dateEpochDay = :day LIMIT 1")
    suspend fun getCompletion(habitId: Long, day: Long): HabitCompletion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCompletion(completion: HabitCompletion)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND dateEpochDay = :day")
    suspend fun deleteCompletion(habitId: Long, day: Long)
}
