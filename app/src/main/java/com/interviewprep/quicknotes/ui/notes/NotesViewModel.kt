package com.interviewprep.quicknotes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interviewprep.quicknotes.data.Note
import com.interviewprep.quicknotes.data.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = true,
    // TODO(feature: search) Add the current search query to the UI state.
)

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private var recentlyDeleted: Note? = null

    // TODO(feature: search) Combine the notes stream with a search query.
    val uiState: StateFlow<NotesUiState> = repository.observeNotes()
        .map { NotesUiState(notes = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NotesUiState(),
        )

    fun onTogglePin(note: Note) {
        viewModelScope.launch { repository.togglePin(note) }
    }

    fun onDelete(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            recentlyDeleted = note
        }
    }

    fun onUndoDelete() {
        val note = recentlyDeleted ?: return
        recentlyDeleted = null
        // Re-insert with the original id so it lands back in the same place.
        viewModelScope.launch { repository.restoreNote(note) }
    }
}
