package com.interviewprep.quicknotes.ui.edit

import androidx.lifecycle.SavedStateHandle
import com.interviewprep.quicknotes.data.Note
import com.interviewprep.quicknotes.testing.FakeNoteRepository
import com.interviewprep.quicknotes.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditNoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `new note is saved with trimmed text`() = runTest {
        val repository = FakeNoteRepository()
        val viewModel = EditNoteViewModel(SavedStateHandle(), repository)

        viewModel.onTitleChange("  Hello  ")
        viewModel.onContentChange("World ")
        viewModel.onSave()

        assertTrue(viewModel.uiState.value.isSaved)
        val saved = repository.current.single()
        assertEquals("Hello", saved.title)
        assertEquals("World", saved.content)
    }

    @Test
    fun `blank note cannot be saved`() = runTest {
        val repository = FakeNoteRepository()
        val viewModel = EditNoteViewModel(SavedStateHandle(), repository)

        viewModel.onTitleChange("   ")
        viewModel.onSave()

        assertFalse(viewModel.uiState.value.canSave)
        assertTrue(repository.current.isEmpty())
    }

    @Test
    fun `existing note is loaded and updated in place`() = runTest {
        val repository = FakeNoteRepository(listOf(Note(id = 7, title = "Old", content = "Body")))
        val viewModel = EditNoteViewModel(
            SavedStateHandle(mapOf(EditNoteViewModel.NOTE_ID_ARG to 7L)),
            repository,
        )

        assertEquals("Old", viewModel.uiState.value.title)
        assertFalse(viewModel.uiState.value.isNewNote)

        viewModel.onTitleChange("New")
        viewModel.onSave()

        val saved = repository.current.single()
        assertEquals(7L, saved.id)
        assertEquals("New", saved.title)
    }
}
