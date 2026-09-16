package com.habitmood.tracker

import android.app.Application
import com.habitmood.tracker.data.AppDatabase
import com.habitmood.tracker.data.HabitRepository

/**
 * Simple hand-rolled DI container. The whole app is small enough that a
 * dependency injection framework (Hilt/Koin) would be overkill.
 */
class HabitMoodApp : Application() {

    lateinit var repository: HabitRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(this)
        repository = HabitRepository(database.habitDao(), database.moodDao())
    }
}
