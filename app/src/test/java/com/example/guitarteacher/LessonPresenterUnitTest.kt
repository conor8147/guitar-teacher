package com.example.guitarteacher

import android.content.Context
import com.example.guitarteacher.data.AppRepository
import com.example.guitarteacher.domain.Fretboard
import com.example.guitarteacher.utils.Timer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.Exception

class LessonPresenterUnitTest {
    private lateinit var lessonPresenter: LessonPresenter
    private val lessonView: LessonContract.View = mock()
    private val fretboard: Fretboard = mock()
    private val repository: AppRepository = mock()
    private val timerFactory: Timer.Factory = mock()
    private val context: Context = mock()

    @ExperimentalCoroutinesApi
    private val coroutineScope = TestCoroutineScope()
    private var lessonTimer: TestTimer? = null
    private var noteCountdown: TestTimer? = null

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        whenever(lessonView.getCoroutineScope()).thenReturn(coroutineScope)
        whenever(repository.getTotalTime()).thenReturn(30)
        whenever(timerFactory.createTimer(any(), any(), any(), any())).thenAnswer { i ->
            val newTestTimer = TestTimer(i.getArgument(0), i.getArgument(1), i.getArgument(2), i.getArgument(3))
            when {
                lessonTimer == null -> lessonTimer = newTestTimer
                noteCountdown == null -> noteCountdown = newTestTimer
                else -> throw Exception("Max of 2 countdowns should have been created")
            }
            newTestTimer
        }

        lessonPresenter = LessonPresenter(
            view = lessonView,
            fretboard = fretboard,
            repository = repository,
            timerFactory = timerFactory,
            applicationContext = context,
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `starting lesson starts both timers`() {
        lessonPresenter.startLesson()
        assert(lessonTimer?.running == true)
        assert(noteCountdown?.running == true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `pausing lesson pauses both timers`() {
        lessonPresenter.startLesson()
        lessonPresenter.pauseLesson()
        coroutineScope.advanceTimeBy(100)
        assert(lessonTimer?.paused == true)
        assert(noteCountdown?.paused == true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `pausing lesson then resuming restarts both timers`() {
        lessonPresenter.startLesson()
        lessonPresenter.pauseLesson()
        lessonPresenter.resumeLesson()
        coroutineScope.advanceTimeBy(100)
        assert(lessonTimer?.running == true)
        assert(noteCountdown?.running == true)
    }



}