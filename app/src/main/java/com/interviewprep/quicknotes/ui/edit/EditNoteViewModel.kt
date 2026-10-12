package com.interviewprep.quicknotes.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interviewprep.quicknotes.data.Note
import com.interviewprep.quicknotes.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditNoteUiState(
    val title: String = "",
    val content: String = "",
    val isNewNote: Boolean = true,
    val isSaved: Boolean = false,
) {
    val canSave: Boolean get() = title.isNotBlank() || content.isNotBlank()
}

class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: NoteRepository,
) : ViewModel() {

    private val noteId: Long = savedStateHandle.get<Long>(NOTE_ID_ARG) ?: NEW_NOTE_ID
    private var existingNote: Note? = null

    private val _uiState = MutableStateFlow(EditNoteUiState(isNewNote = noteId == NEW_NOTE_ID))
    val uiState: StateFlow<EditNoteUiState> = _uiState.asStateFlow()

    init {
        if (noteId != NEW_NOTE_ID) {
            viewModelScope.launch {
                repository.getNote(noteId)?.let { note ->
                    existingNote = note
                    _uiState.update { it.copy(title = note.title, content = note.content) }
                }
            }
        }
    }

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value) }

    fun onContentChange(value: String) = _uiState.update { it.copy(content = value) }

    fun onSave() {
        val state = _uiState.value
        if (!state.canSave) return
        viewModelScope.launch {
            val note = existingNote?.copy(title = state.title.trim(), content = state.content.trim())
                ?: Note(title = state.title.trim(), content = state.content.trim())
            repository.saveNote(note)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    companion object {
        const val NOTE_ID_ARG = "noteId"
        const val NEW_NOTE_ID = -1L
    }
}
