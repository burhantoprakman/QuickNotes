package com.interviewprep.quicknotes.testing

import com.interviewprep.quicknotes.data.Note
import com.interviewprep.quicknotes.data.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** In-memory repository that mirrors the Room ordering (pinned first, newest first). */
class FakeNoteRepository(initial: List<Note> = emptyList()) : NoteRepository {

    private val notes = MutableStateFlow(initial)
    private var nextId = (initial.maxOfOrNull { it.id } ?: 0L) + 1
    private var clock = 1_000L

    val current: List<Note> get() = notes.value

    override fun observeNotes(): Flow<List<Note>> =
        notes.map { list -> list.sortedWith(compareByDescending<Note> { it.isPinned }.thenByDescending { it.updatedAt }) }

    override suspend fun getNote(id: Long): Note? = notes.value.firstOrNull { it.id == id }

    override suspend fun saveNote(note: Note): Long {
        val now = clock++
        return if (note.id == 0L) {
            val id = nextId++
            notes.update { it + note.copy(id = id, createdAt = now, updatedAt = now) }
            id
        } else {
            notes.update { list -> list.map { if (it.id == note.id) note.copy(updatedAt = now) else it } }
            note.id
        }
    }

    override suspend fun deleteNote(note: Note) {
        notes.update { list -> list.filterNot { it.id == note.id } }
    }

    override suspend fun togglePin(note: Note) {
        notes.update { list -> list.map { if (it.id == note.id) it.copy(isPinned = !it.isPinned) else it } }
    }

    override suspend fun restoreNote(note: Note) {
        notes.update { list -> list.filterNot { it.id == note.id } + note }
    }
}
