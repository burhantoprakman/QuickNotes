package com.interviewprep.quicknotes.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.interviewprep.quicknotes.ui.edit.EditNoteScreen
import com.interviewprep.quicknotes.ui.edit.EditNoteViewModel
import com.interviewprep.quicknotes.ui.notes.NotesScreen

private const val NOTES_ROUTE = "notes"
private const val EDIT_ROUTE = "edit"

@Composable
fun QuickNotesNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NOTES_ROUTE) {
        composable(NOTES_ROUTE) {
            NotesScreen(
                onAddNote = { navController.navigate("$EDIT_ROUTE?${EditNoteViewModel.NOTE_ID_ARG}=-1") },
                onNoteClick = { id -> navController.navigate("$EDIT_ROUTE?${EditNoteViewModel.NOTE_ID_ARG}=$id") },
            )
        }
        composable(
            route = "$EDIT_ROUTE?${EditNoteViewModel.NOTE_ID_ARG}={${EditNoteViewModel.NOTE_ID_ARG}}",
            arguments = listOf(
                navArgument(EditNoteViewModel.NOTE_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            ),
        ) {
            EditNoteScreen(onDone = { navController.popBackStack() })
        }
    }
}
