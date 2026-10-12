package com.interviewprep.quicknotes

import android.content.Context
import com.interviewprep.quicknotes.data.NoteDatabase
import com.interviewprep.quicknotes.data.NoteRepository
import com.interviewprep.quicknotes.data.OfflineNoteRepository

/** Manual dependency injection. Easy to swap for Hilt if asked in the interview. */
class AppContainer(context: Context) {
    val noteRepository: NoteRepository by lazy {
        OfflineNoteRepository(NoteDatabase.getInstance(context).noteDao())
    }
}
