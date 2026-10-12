# QuickNotes

A small, modern Android notes app used for interview practice. It is deliberately
compact so you can read the whole codebase in a few minutes and then extend it.

## Features

- Create, edit and delete notes
- Pin notes (pinned notes stay on top)
- Undo delete via snackbar
- Notes persist locally with Room
- Material 3 with dynamic color and dark theme

**Not implemented (your task):** search — see [`FEATURE_TASK.md`](FEATURE_TASK.md).

## Tech stack

| Concern | Choice |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM, unidirectional data flow (`StateFlow` UI state) |
| Persistence | Room (with KSP) |
| Navigation | Navigation Compose |
| DI | Manual (`AppContainer`) — easy to swap for Hilt |
| Tests | JUnit 4, kotlinx-coroutines-test, fake repository |

## Project structure

```
app/src/main/java/com/interviewprep/quicknotes/
├── QuickNotesApplication.kt   # creates the AppContainer
├── AppContainer.kt            # manual DI
├── MainActivity.kt
├── data/
│   ├── Note.kt                # Room entity
│   ├── NoteDao.kt
│   ├── NoteDatabase.kt
│   └── NoteRepository.kt      # interface + Room implementation
└── ui/
    ├── Navigation.kt
    ├── AppViewModelProvider.kt # ViewModel factory
    ├── theme/Theme.kt
    ├── notes/                 # list screen + ViewModel
    └── edit/                  # add/edit screen + ViewModel
```

## Getting started

Requirements: Android Studio (Ladybug or newer), JDK 17, Android SDK 35.

```bash
git clone <this-repo>
cd QuickNotes
./gradlew assembleDebug        # build
./gradlew testDebugUnitTest    # run unit tests
```

Or open the folder in Android Studio and press Run.
