# Smart Habit & Mood Tracker

A minimal, native Android habit + mood tracker built with **Kotlin** and **Jetpack Compose
(Material 3)**. Every day you check off your habits, log a mood emoji, and get a playful,
personalized motivational message — generated **entirely on-device**, no network access,
no API key, no account.

## Highlights

- **Material 3** UI with dynamic color (Android 12+ "Material You"), light/dark support,
  rounded cards, smooth animated toggles, and a bottom-nav app shell.
- **Today** tab — daily checklist of habits with animated completion states, streak
  flame badges, an emoji mood picker, and a "pep talk" card.
- **Habits** tab — add habits with a custom emoji, color, and weekly target; archive any
  habit you no longer track.
- **Insights** tab — a 7-day completion bar chart and a mood trend line, drawn with
  Compose `Canvas`.
- **On-device motivation engine** (`motivation/MotivationEngine.kt`) — a template-based
  text generator that reacts to your completion rate, mood, and best current streak to
  produce a varied, playful message. No LLM download, no network round-trip, instant and
  free. The function signature is intentionally small so you could later swap in a real
  generative model (e.g. Android's on-device GenAI / AICore APIs, where supported by the
  device) without touching any UI code.
- **Room** database (`data/`) for habits, daily completions, and mood entries — all local
  SQLite, nothing leaves the device.

## Project structure

```
app/src/main/java/com/habitmood/tracker/
├── HabitMoodApp.kt            # Application class, wires DB + repository
├── MainActivity.kt            # Bottom-nav Scaffold + NavHost
├── data/                      # Room entities, DAOs, database, repository
├── motivation/                # The local motivational text generator
├── navigation/                # Screen route definitions
├── viewmodel/                 # MainViewModel + factory
└── ui/
    ├── theme/                 # Material 3 color scheme, type scale
    ├── components/            # HabitCard, MoodSelector, MotivationCard, AddHabitDialog
    └── screens/                # TodayScreen, HabitsScreen, InsightsScreen, SettingsScreen
```

## Requirements

- Android Studio (Ladybug/Koala or newer recommended)
- Android SDK Platform **36** installed (Tools → SDK Manager)
- JDK 17 (Android Studio bundles a compatible JDK automatically)

## Opening the project

1. Unzip the project and open the root folder (`SmartHabitMoodTracker/`) in Android Studio.
2. Let Android Studio sync Gradle. This project doesn't ship a pre-built
   `gradlew`/`gradle-wrapper.jar` binary (not something safe to hand you as opaque
   binary bytes) — Android Studio will offer to regenerate the wrapper automatically on
   first sync. If it doesn't, run `gradle wrapper --gradle-version 8.9` once from a
   terminal with Gradle installed, or just use **Android Studio's bundled Gradle** via
   the "Gradle JVM"/"Use Gradle from" setting in Settings → Build Tools → Gradle.
3. Run on an emulator or device (`minSdk 26`, `targetSdk`/`compileSdk 36`).

## Notes & extension ideas

- **Persistence for Settings**: the Settings screen's toggles are currently in-memory
  (`remember { mutableStateOf(...) }`) as a clean placeholder. Wire them to
  `androidx.datastore:datastore-preferences` (already a dependency) for persistence
  across app restarts.
- **Real daily reminders**: the "Daily reminder" toggle is a UI placeholder. To make it
  functional, schedule a `WorkManager` periodic job or an exact `AlarmManager` alarm that
  posts a notification (remember to request `POST_NOTIFICATIONS` on Android 13+).
- **Editing/deleting habits**: currently you can add and archive; extend `HabitsScreen`
  with an edit dialog (reusing `AddHabitDialog`) and a real delete option if you don't
  want to keep archived history.
- **True generative AI feedback**: `MotivationEngine.generate()` is template-based by
  design (instant, free, offline). If you want actual generative text, this is the single
  seam to change — call an on-device model (Android's ML Kit GenAI APIs / AICore where
  the device supports it) or a cloud LLM API from that function instead.
