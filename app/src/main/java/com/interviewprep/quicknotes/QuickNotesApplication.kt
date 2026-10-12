package com.interviewprep.quicknotes

import android.app.Application

class QuickNotesApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
