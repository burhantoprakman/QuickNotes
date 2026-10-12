package com.interviewprep.quicknotes.ui.notes

import com.interviewprep.quicknotes.data.Note
import com.interviewprep.quicknotes.testing.FakeNoteRepository
import com.interviewprep.quicknotes.testing.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NotesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val noteA = Note(id = 1, title = "Alpha", content = "first", updatedAt = 1)
    private val noteB = Note(id = 2, title = "Beta", content = "second", updatedAt = 2)

    @Test
    fun `emits notes newest first once loaded`() = runTest {
        val viewModel = NotesViewModel(FakeNoteRepository(listOf(noteA, noteB)))
        backgroundScope.launch(mainDispatcherRule.dispatcher) { viewModel.uiState.collect() }

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(listOf(2L, 1L), state.notes.map { it.id })
    }

    @Test
    fun `pinned note moves to the top`() = runTest {
        val viewModel = NotesViewModel(FakeNoteRepository(listOf(noteA, noteB)))
        backgroundScope.launch(mainDispatcherRule.dispatcher) { viewModel.uiState.collect() }

        viewModel.onTogglePin(noteA)

        val first = viewModel.uiState.value.notes.first()
        assertEquals(1L, first.id)
        assertTrue(first.isPinned)
    }

    @Test
    fun `delete then undo restores the note`() = runTest {
        val repository = FakeNoteRepository(listOf(noteA, noteB))
        val viewModel = NotesViewModel(repository)
        backgroundScope.launch(mainDispatcherRule.dispatcher) { viewModel.uiState.collect() }

        viewModel.onDelete(noteA)
        assertEquals(listOf(2L), viewModel.uiState.value.notes.map { it.id })

        viewModel.onUndoDelete()
        assertEquals(listOf(2L, 1L), viewModel.uiState.value.notes.map { it.id })
    }

    // TODO(feature: search) Add tests: empty query shows all, query matches title,
    //  query matches content, matching is case-insensitive, no matches -> empty list.
}
