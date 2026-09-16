package com.habitmood.tracker.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.habitmood.tracker.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun InsightsScreen(viewModel: MainViewModel) {
    val weekly by viewModel.weeklyCompletionFlow()
        .collectAsStateWithLifecycle(initialValue = emptyList<LocalDate>() to emptyMap())
    val moods by viewModel.weeklyMoodFlow().collectAsStateWithLifecycle(initialValue = emptyMap())
    val totalHabits by viewModel.allHabits.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your last 7 days", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))

        Card(shape = MaterialTheme.shapes.large) {
            Column(Modifier.padding(16.dp)) {
                Text("Habits completed", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))

                val (days, counts) = weekly
                val maxCount = totalHabits.size.coerceAtLeast(1)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    days.forEach { day ->
                        val count = counts[day] ?: 0
                        val fraction = (count.toFloat() / maxCount).coerceIn(0.05f, 1f)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        ) {
                            Text("$count", style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .fillMaxHeight(fraction)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(6.dp)
                                    )
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = day.format(DateTimeFormatter.ofPattern("EEE")),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Card(shape = MaterialTheme.shapes.large) {
            Column(Modifier.padding(16.dp)) {
                Text("Mood trend", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))

                if (moods.isEmpty()) {
                    Text("Log your mood on the Today tab to see trends here.")
                } else {
                    val sortedDays = moods.keys.sorted()
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        val stepX = size.width / (sortedDays.size - 1).coerceAtLeast(1)
                        val points = sortedDays.mapIndexed { index, day ->
                            val score = moods[day] ?: 3
                            val y = size.height - (score / 5f) * size.height
                            Offset(index * stepX, y)
                        }
                        for (i in 0 until points.size - 1) {
                            drawLine(
                                color = Color(0xFF00A896),
                                start = points[i],
                                end = points[i + 1],
                                strokeWidth = 6f
                            )
                        }
                        points.forEach { point ->
                            drawCircle(color = Color(0xFF00A896), radius = 8f, center = point)
                        }
                    }
                }
            }
        }
    }
}
