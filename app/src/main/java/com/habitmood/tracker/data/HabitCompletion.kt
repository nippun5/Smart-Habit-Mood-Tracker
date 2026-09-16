package com.habitmood.tracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_completions",
    indices = [Index(value = ["habitId", "dateEpochDay"], unique = true)]
)
data class HabitCompletion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val dateEpochDay: Long,
    val completed: Boolean,
    val completedAtMillis: Long = System.currentTimeMillis()
)
