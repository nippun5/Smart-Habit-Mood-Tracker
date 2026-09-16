package com.habitmood.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.habitmood.tracker.navigation.Screen
import com.habitmood.tracker.ui.screens.HabitsScreen
import com.habitmood.tracker.ui.screens.InsightsScreen
import com.habitmood.tracker.ui.screens.SettingsScreen
import com.habitmood.tracker.ui.screens.TodayScreen
import com.habitmood.tracker.ui.theme.SmartHabitMoodTheme
import com.habitmood.tracker.viewmodel.MainViewModel
import com.habitmood.tracker.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as HabitMoodApp).repository

        setContent {
            SmartHabitMoodTheme {
                val viewModel: MainViewModel = viewModel(factory = ViewModelFactory(repository))
                AppScaffold(viewModel)
            }
        }
    }
}

private data class BottomItem(val screen: Screen, val icon: ImageVector)

@Composable
fun AppScaffold(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        BottomItem(Screen.Today, Icons.Filled.CheckCircle),
        BottomItem(Screen.Habits, Icons.Filled.List),
        BottomItem(Screen.Insights, Icons.Filled.Insights),
        BottomItem(Screen.Settings, Icons.Filled.Settings)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.screen.label) },
                        label = { Text(item.screen.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Today.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Today.route) { TodayScreen(viewModel) }
            composable(Screen.Habits.route) { HabitsScreen(viewModel) }
            composable(Screen.Insights.route) { InsightsScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}
