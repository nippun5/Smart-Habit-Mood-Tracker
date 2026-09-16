package com.habitmood.tracker.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

private val moods = listOf(
    1 to "\uD83D\uDE2D", // crying
    2 to "\uD83D\uDE1F", // worried
    3 to "\uD83D\uDE10", // neutral
    4 to "\uD83D\uDE42", // slight smile
    5 to "\uD83D\uDE04"  // big smile
)

@Composable
fun MoodSelector(
    selected: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        moods.forEach { (score, emoji) ->
            val isSelected = selected == score
            val bubbleSize by animateDpAsState(
                targetValue = if (isSelected) 52.dp else 44.dp,
                label = "moodBubbleSize"
            )
            Box(
                modifier = Modifier
                    .size(bubbleSize)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onSelect(score) },
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
