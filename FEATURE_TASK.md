# Feature task: Search notes

This feature is intentionally **not implemented**. Build it end to end.

## User story

As a user with many notes, I want to type into a search field on the notes list
so that I only see notes whose **title or content** contains my query.

## Acceptance criteria

1. The notes list screen has a search field (in or below the top app bar).
2. Typing filters the list live; there is no "submit" button.
3. Matching is **case-insensitive** and checks both `title` and `content`.
4. An empty or blank query shows all notes.
5. Pinned-first, newest-first ordering is preserved in the filtered results.
6. When nothing matches, show a distinct empty state, e.g. `No notes match "kotlin"`
   (different from the "No notes yet" message).
7. A clear (✕) button empties the query.
8. The query survives rotation (configuration change).
9. Unit tests cover the filtering logic in `NotesViewModel`.

## Where to start

Search the project for `TODO(feature: search)`. There are markers in:

| Layer | File |
|---|---|
| Data (Room) | `data/NoteDao.kt` |
| Repository | `data/NoteRepository.kt` (and `testing/FakeNoteRepository.kt` in tests) |
| ViewModel | `ui/notes/NotesViewModel.kt` |
| UI | `ui/notes/NotesScreen.kt` |
| Tests | `ui/notes/NotesViewModelTest.kt` |

## Design decisions worth talking through

- **Filter in SQL or in memory?** A Room `LIKE` query scales better; filtering the
  existing `Flow<List<Note>>` in the ViewModel is simpler and easy to unit test.
  Either is fine — be ready to justify the choice.
- **Combining streams:** `combine(notesFlow, queryFlow)` vs `queryFlow.flatMapLatest { repo.search(it) }`.
- **Debounce:** worth adding for a SQL query (`debounce(300)`); usually unnecessary for in-memory filtering.
- **Surviving rotation:** keep the query in the ViewModel (or `SavedStateHandle` to also survive process death).
- **LIKE wildcards:** if the user types `%` or `_`, should they be escaped?

## Stretch goals

- Highlight the matched text in results.
- Filter chips: All / Pinned.
