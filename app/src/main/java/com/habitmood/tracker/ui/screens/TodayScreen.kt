package com.habitmood.tracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.habitmood.tracker.ui.components.HabitCard
import com.habitmood.tracker.ui.components.MoodSelector
import com.habitmood.tracker.ui.components.MotivationCard
import com.habitmood.tracker.viewmodel.MainViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = state.date.format(DateTimeFormatter.ofPattern("EEEE, MMM d")),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${state.habits.count { it.completedToday }} of ${state.habits.size} habits done",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            MotivationCard(
                message = state.motivation,
                onRegenerate = { viewModel.regenerateMotivation() }
            )
        }

        item {
            Card(shape = MaterialTheme.shapes.large) {
                Column(Modifier.padding(16.dp)) {
                    Text("How are you feeling today?", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    MoodSelector(
                        selected = state.mood,
                        onSelect = { viewModel.setMood(it) }
                    )
                }
            }
        }

        if (state.habits.isEmpty() && !state.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No habits yet. Head to the Habits tab to add your first one \uD83C\uDF31",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(state.habits, key = { it.habit.id }) { item ->
                HabitCard(
                    habit = item.habit,
                    completed = item.completedToday,
                    streak = item.streak,
                    onToggle = { viewModel.toggleHabit(item.habit.id) }
                )
            }
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}
