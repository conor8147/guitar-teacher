package com.example.guitarteacher

import kotlinx.coroutines.CoroutineScope

interface LessonContract {

    interface View {
        fun setProgressMax(millis: Long)

        fun updateProgress(millisRemaining: Long)

        fun updateHeading(newHeading: String)

        fun updateMainText(text: String)

        fun updateLessonTimer(text: String)

        fun navigateUp()

        fun getCoroutineScope(): CoroutineScope
    }

    interface Presenter {
        fun startLesson()

        fun pauseLesson()

        fun resumeLesson()

        fun endLesson()
    }
}