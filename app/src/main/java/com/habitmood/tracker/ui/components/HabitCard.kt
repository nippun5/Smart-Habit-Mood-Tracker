package com.habitmood.tracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.habitmood.tracker.data.Habit

@Composable
fun HabitCard(
    habit: Habit,
    completed: Boolean,
    streak: Int,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val habitColor = remember(habit.colorHex) {
        runCatching { Color(android.graphics.Color.parseColor(habit.colorHex)) }
            .getOrDefault(Color(0xFF43AA8B))
    }
    val containerColor by animateColorAsState(
        targetValue = if (completed) habitColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
        label = "habitCardContainer"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (completed) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(habitColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(habit.emoji, style = MaterialTheme.typography.titleLarge)
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None
                )
                if (streak > 0) {
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFF6AE2D),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "$streak day${if (streak == 1) "" else "s"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (completed) habitColor else Color.Transparent)
                    .border(
                        width = if (completed) 0.dp else 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
