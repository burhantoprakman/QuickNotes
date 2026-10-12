package com.interviewprep.quicknotes.data

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun observeNotes(): Flow<List<Note>>
    suspend fun getNote(id: Long): Note?
    suspend fun saveNote(note: Note): Long
    suspend fun deleteNote(note: Note)
    suspend fun togglePin(note: Note)

    /** Re-inserts a deleted note as-is (same id and timestamps). Used for undo. */
    suspend fun restoreNote(note: Note)

    // TODO(feature: search) Expose a way to observe notes matching a query.
}

class OfflineNoteRepository(private val dao: NoteDao) : NoteRepository {

    override fun observeNotes(): Flow<List<Note>> = dao.observeAll()

    override suspend fun getNote(id: Long): Note? = dao.getById(id)

    override suspend fun saveNote(note: Note): Long {
        val now = System.currentTimeMillis()
        return if (note.id == 0L) {
            dao.insert(note.copy(createdAt = now, updatedAt = now))
        } else {
            dao.update(note.copy(updatedAt = now))
            note.id
        }
    }

    override suspend fun deleteNote(note: Note) = dao.delete(note)

    override suspend fun togglePin(note: Note) =
        dao.update(note.copy(isPinned = !note.isPinned))

    override suspend fun restoreNote(note: Note) {
        dao.insert(note)
    }
}
