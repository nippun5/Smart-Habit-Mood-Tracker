package com.habitmood.tracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mood_entries",
    indices = [Index(value = ["dateEpochDay"], unique = true)]
)
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val moodScore: Int, // 1 (rough day) .. 5 (great day)
    val note: String = "",
    val timestampMillis: Long = System.currentTimeMillis()
)
