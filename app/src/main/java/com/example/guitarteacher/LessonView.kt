package com.example.guitarteacher

interface LessonView {

    fun setProgressMax(millis: Long)

    fun updateProgress(millisRemaining: Long)

    fun updateHeading(newHeading: String)

    fun updateMainText(text: String)

    fun updateLessonTimer(text: String)

    fun navigateUp()
}