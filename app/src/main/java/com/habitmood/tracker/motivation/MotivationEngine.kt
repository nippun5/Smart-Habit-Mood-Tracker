package com.habitmood.tracker.motivation

/**
 * Snapshot of a day's data used to generate a motivational message.
 */
data class DailyStats(
    val totalHabits: Int,
    val completedHabits: Int,
    val bestStreak: Int,
    val bestStreakHabitName: String?,
    val moodScore: Int? // 1..5, null if mood not logged yet today
)

/**
 * A fully on-device, offline "AI" text generator. No network calls, no API key,
 * no data ever leaves the phone. It works by picking from weighted template
 * banks based on the day's stats and recombining fragments, so messages feel
 * varied and personal rather than copy-pasted from a single fixed string.
 *
 * This is intentionally template-based rather than a full LLM: it's instant,
 * free, works fully offline, and needs no model download. If you later want a
 * true generative model, Android's on-device GenAI APIs (ML Kit GenAI / AICore,
 * where supported) could be swapped in behind this same `generate()` signature.
 */
object MotivationEngine {

    private val celebrationOpeners = listOf(
        "Look at you go!",
        "Absolute legend behavior.",
        "Okay, superstar.",
        "Chef's kiss.",
        "You're on fire today \uD83D\uDD25",
        "Certified habit crusher.",
        "This is the energy we love."
    )

    private val encouragementOpeners = listOf(
        "Small steps still count.",
        "Progress, not perfection.",
        "Every rep matters.",
        "You showed up — that's the hard part.",
        "Not bad at all.",
        "Steady wins the race."
    )

    private val comebackOpeners = listOf(
        "Tomorrow's a fresh page.",
        "Rough day? We've all had one.",
        "Reset, don't quit.",
        "One off day doesn't erase your progress.",
        "Dust yourself off, champ."
    )

    private val moodFlavor = mapOf(
        5 to listOf("and your mood is glowing today \u2728", "riding a great mood wave too!"),
        4 to listOf("with a solid mood to match", "good vibes all around"),
        3 to listOf("even on a so-so mood day", "middle-of-the-road mood, top-tier effort"),
        2 to listOf("even though today felt heavy", "despite the rough mood — respect"),
        1 to listOf("even on a genuinely hard day, you still tried", "tough mood day, real courage")
    )

    private val closers = listOf(
        "Keep the streak alive.",
        "See you tomorrow.",
        "Same time tomorrow?",
        "One day at a time.",
        "You've got this.",
        "Onward! \uD83D\uDE80",
        "Stack another brick on the wall."
    )

    private val streakLineTemplates = listOf(
        { name: String, days: Int -> "Your \"$name\" streak is at $days days \uD83D\uDD25 — don't jinx it." },
        { name: String, days: Int -> "$days-day streak on \"$name\"? Iconic." },
        { name: String, days: Int -> "\"$name\" is $days days strong. Legendary consistency." }
    )

    /** A full, warm pep-talk message for the main card on the Today screen. */
    fun generate(stats: DailyStats): String {
        val rate = if (stats.totalHabits == 0) 0f else stats.completedHabits.toFloat() / stats.totalHabits
        val opener = pickOpener(rate, stats.totalHabits)

        return buildString {
            append(opener)
            append(" ")
            if (stats.totalHabits > 0) {
                append("You completed ${stats.completedHabits}/${stats.totalHabits} habits today")
            } else {
                append("No habits logged yet today")
            }

            stats.moodScore?.let { mood ->
                moodFlavor[mood]?.random()?.let { flavor -> append(", $flavor") }
            }
            append(". ")

            if (stats.bestStreak >= 2 && stats.bestStreakHabitName != null) {
                append(streakLineTemplates.random().invoke(stats.bestStreakHabitName, stats.bestStreak))
                append(" ")
            }

            append(closers.random())
        }
    }

    /** A short one-liner, handy for compact UI like a notification or widget. */
    fun generateShort(stats: DailyStats): String {
        val rate = if (stats.totalHabits == 0) 0f else stats.completedHabits.toFloat() / stats.totalHabits
        return pickOpener(rate, stats.totalHabits)
    }

    private fun pickOpener(rate: Float, totalHabits: Int): String = when {
        rate >= 0.999f && totalHabits > 0 -> celebrationOpeners.random()
        rate >= 0.5f -> encouragementOpeners.random()
        else -> comebackOpeners.random()
    }
}
