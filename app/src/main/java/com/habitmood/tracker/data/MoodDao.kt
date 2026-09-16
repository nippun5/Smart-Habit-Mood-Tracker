package com.habitmood.tracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Query("SELECT * FROM mood_entries WHERE dateEpochDay = :day LIMIT 1")
    fun getMoodForDay(day: Long): Flow<MoodEntry?>

    @Query("SELECT * FROM mood_entries WHERE dateEpochDay BETWEEN :start AND :end ORDER BY dateEpochDay ASC")
    fun getMoodsBetween(start: Long, end: Long): Flow<List<MoodEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMood(entry: MoodEntry)
}
