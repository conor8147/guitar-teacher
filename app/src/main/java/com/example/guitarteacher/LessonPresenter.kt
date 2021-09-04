package com.example.guitarteacher

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.guitarteacher.data.AppRepository
import com.example.guitarteacher.domain.Fretboard
import com.example.guitarteacher.domain.Note
import com.example.guitarteacher.utils.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.BufferOverflowException
import javax.inject.Inject

class LessonPresenter @Inject constructor(
    private val view: LessonContract.View,
    private val fretboard: Fretboard,
    private val repository: AppRepository,
    private val timerFactory: Timer.Factory,
    private val applicationContext: Context,
): LessonContract.Presenter {

    private val coroutineScope: CoroutineScope = view.getCoroutineScope()
    @VisibleForTesting
    /** Channel to communicate with noteCountdown coroutine when it is paused from the main thread */
    private val countdownChannel: Channel<String> = Channel(Channel.CONFLATED)

    /** Timer for the entire lesson */
    private lateinit var lessonTimer: Timer

    @ExperimentalCoroutinesApi
    override fun startLesson() {
        view.setProgressMax(repository.getTimePerNote())
        lessonTimer = timerFactory.createTimer(
            totalTime = repository.getTotalTime() * 1000L,
            tickLength = LESSON_TIMER_TICK_LENGTH,
            onTick = { millisRemaining -> updateLessonTime(millisRemaining) },
            onFinished = ::endLesson,
        )

        lessonTimer.start()
        startNoteLesson()
    }

    override fun pauseLesson() {
        lessonTimer.pause()
        countdownChannel.trySend(PAUSE)
            .onFailure { throw it ?: BufferOverflowException() }
    }

    override fun resumeLesson() {
        lessonTimer.start()
        countdownChannel.trySend(START)
            .onFailure { throw it ?: BufferOverflowException() }
    }

    override fun endLesson() {
        countdownChannel.trySend(CANCEL)
        countdownChannel.close()
        lessonTimer.cancel() // otherwise the timer will keep running if we navigate back mid-lesson
    }

    private fun updateLessonTime(timeRemaining: Long) {
        val minutesRemaining = timeRemaining / 60_000
        val secondsRemaining = (timeRemaining / 1000) % 60

        view.updateLessonTimer("$minutesRemaining:$secondsRemaining")
    }

    @ExperimentalCoroutinesApi
    private fun startNoteLesson() = coroutineScope.launch {
        var finished: Boolean
        val noteCountdown = timerFactory.createTimer(
            totalTime = repository.getTimePerNote(),
            tickLength = NOTE_COUNTDOWN_TIMER_TICK_LENGTH,
            onTick = { millisRemaining ->
                view.updateProgress(millisRemaining)
            },
            onFinished = { finished = true },
        )

        while (!lessonTimer.isFinished()) {
            finished = false
            val randomNote = getRandomNote()
            noteCountdown.resetCountdown(randomNote)
            while (!finished) {
                noteCountdown.checkChannel()
                delay(100)
            }
            displayAnswer(randomNote)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun Timer.resetCountdown(randomNote: Note) {
        reset()
        start()
        view.updateHeading("${applicationContext.getString(R.string.guitar_string)} ${repository.getGuitarString()}")
        view.updateMainText(randomNote.noteString)
    }

    @ExperimentalCoroutinesApi
    private suspend fun Timer.checkChannel() {
        while (!countdownChannel.isEmpty) {
            when (countdownChannel.receive()) {
                CANCEL -> cancel()
                PAUSE -> pause()
                START -> start()
            }
        }
    }

    private suspend fun displayAnswer(note: Note) {
        val correctFret = fretboard.getFretForNoteOnString(note, repository.getGuitarString())
        view.updateHeading(applicationContext.getString(R.string.fret))
        view.updateMainText(correctFret.toString())
        delay(DISPLAY_ANSWER_DURATION_MILLIS)
    }

    private fun getRandomNote() = Note.values().random()

}

private const val DISPLAY_ANSWER_DURATION_MILLIS = 2000L
private const val LESSON_TIMER_TICK_LENGTH = 500L
private const val NOTE_COUNTDOWN_TIMER_TICK_LENGTH = 10L

// Constants for use with countdown channel
private const val PAUSE = "pause"
private const val CANCEL = "cancel"
private const val START = "start"