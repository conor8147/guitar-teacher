package com.example.guitarteacher

import kotlinx.coroutines.CoroutineScope

interface LessonContract {

    interface View {
        fun setProgressMax(millis: Long)

        fun updateProgress(millisRemaining: Long)

        fun updateLessonTimer(text: String)

        fun navigateUp()

        fun getCoroutineScope(): CoroutineScope

        fun displayNoteAndString(note: String, guitarString: Int)

        fun displayAnswer(correctFret: Int)
    }

    interface Presenter {
        fun startLesson()

        fun pauseLesson()

        fun resumeLesson()

        fun endLesson()
    }
}