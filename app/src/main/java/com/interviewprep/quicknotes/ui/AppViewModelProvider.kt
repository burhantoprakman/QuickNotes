package com.interviewprep.quicknotes.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.interviewprep.quicknotes.QuickNotesApplication
import com.interviewprep.quicknotes.ui.edit.EditNoteViewModel
import com.interviewprep.quicknotes.ui.notes.NotesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { NotesViewModel(app().container.noteRepository) }
        initializer {
            EditNoteViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = app().container.noteRepository,
            )
        }
    }
}

private fun CreationExtras.app(): QuickNotesApplication =
    this[APPLICATION_KEY] as QuickNotesApplication
