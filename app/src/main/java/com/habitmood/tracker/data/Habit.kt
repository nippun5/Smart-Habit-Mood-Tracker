package com.habitmood.tracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val emoji: String,
    val colorHex: String,
    val targetDaysPerWeek: Int = 7,
    val createdAtEpochDay: Long,
    val isArchived: Boolean = false,
    val sortOrder: Int = 0
)
