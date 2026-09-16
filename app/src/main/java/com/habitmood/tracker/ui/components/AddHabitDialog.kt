package com.habitmood.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.habitmood.tracker.ui.theme.HabitPalette

private val emojiOptions = listOf("\uD83D\uDCA7", "\uD83C\uDFC3", "\uD83D\uDCD6", "\uD83E\uDDD8", "\uD83E\uDD57", "\uD83D\uDE34", "\u270D\uFE0F", "\uD83D\uDC8A", "\uD83C\uDFAF", "\uD83E\uDDF9")

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, emoji: String, color: String, targetDays: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf(emojiOptions.first()) }
    var selectedColor by remember { mutableStateOf(HabitPalette.first()) }
    var targetDays by remember { mutableFloatStateOf(7f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New habit") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
                Text("Pick an icon", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(emojiOptions) { emoji ->
                        val isSelected = emoji == selectedEmoji
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) { Text(emoji) }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Pick a color", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(HabitPalette) { hex ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        val isSelected = hex == selectedColor
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Target: ${targetDays.toInt()} days/week", style = MaterialTheme.typography.labelLarge)
                Slider(
                    value = targetDays,
                    onValueChange = { targetDays = it },
                    valueRange = 1f..7f,
                    steps = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name.trim(), selectedEmoji, selectedColor, targetDays.toInt())
                    }
                },
                enabled = name.isNotBlank()
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
