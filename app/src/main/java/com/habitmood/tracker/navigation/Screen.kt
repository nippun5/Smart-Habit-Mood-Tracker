package com.habitmood.tracker.navigation

sealed class Screen(val route: String, val label: String) {
    data object Today : Screen("today", "Today")
    data object Habits : Screen("habits", "Habits")
    data object Insights : Screen("insights", "Insights")
    data object Settings : Screen("settings", "Settings")
}
