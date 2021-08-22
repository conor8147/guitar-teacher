package com.example.guitarteacher

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.guitarteacher.domain.Fretboard
import com.example.guitarteacher.domain.Note
import com.example.guitarteacher.utils.Timer
import com.example.guitarteacher.utils.TimerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.properties.Delegates.notNull

@AndroidEntryPoint
class LessonFragment : Fragment() {

    @Inject lateinit var fretboard: Fretboard
    @Inject lateinit var repository: AppRepository
    @Inject lateinit var timerFactory: TimerFactory

    lateinit var remainingTimePb: ProgressBar
    lateinit var headingTv:TextView
    lateinit var mainTv: TextView
    lateinit var lessonTimeTv: TextView

    private var timePerNote by notNull<Long>()
    private var guitarString by notNull<Int>()

    /** Timer for the entire lesson */
    private lateinit var lessonTimer: Timer

    override fun onAttach(context: Context) {
        super.onAttach(context)
        timePerNote = repository.getTimePerNote() * 1000L // TODO get rid of these magic numbers, all time should be converted to millis before it is stored by the repo, and use millis everywhere internally.
        guitarString = repository.getGuitarString()

        lessonTimer = timerFactory.createTimer(
            totalTime = repository.getTotalTime() * 1000L,
            tickLength = LESSON_TIMER_TICK_LENGTH,
            onTick = {},
            // Weird notation, but this is how we can pass one function into another function as a parameter.
            // this allows our lessonTimer to have a reference to finish(), so that it can call it with
            // finish.invoke() whenever it wants to.
            onFinished = ::finish,
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lesson, container, false).apply {
        remainingTimePb = findViewById(R.id.remainingTimePb)
        mainTv = findViewById(R.id.mainTv)
        lessonTimeTv = findViewById(R.id.lessonTimeTv)
        headingTv = findViewById(R.id.headingTv)

        remainingTimePb.max = timePerNote.toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runLesson()
    }

    override fun onDestroy() {
        super.onDestroy()
        lessonTimer.cancel() // otherwise the timer will keep running if we navigate back mid-lesson
    }

    private fun runLesson() {
        lessonTimer.start()
        startNoteLesson()
    }

    private fun startNoteLesson() {
        lifecycleScope.launch {
            var finished: Boolean

            val noteCountdown = timerFactory.createTimer(
                totalTime = timePerNote,
                tickLength = NOTE_COUNTDOWN_TIMER_TICK_LENGTH,
                onTick = { millisRemaining ->
                    remainingTimePb.progress = millisRemaining.toInt()
                },
                onFinished = { finished = true },
            )
            while (true) {
                finished = false
                noteCountdown.reset()
                noteCountdown.start()
                val randomNote = getRandomNote()
                headingTv.text = "${getString(R.string.guitar_string)} $guitarString"
                mainTv.text = randomNote.noteString

                while (!finished) { delay(100) }
                displayAnswer(randomNote)
            }
        }
    }

    private suspend fun displayAnswer(note: Note) {
        val correctFret = fretboard.getFretForNoteOnString(note, guitarString)
        headingTv.text = getString(R.string.fret)
        mainTv.text = correctFret.toString()
        delay(DISPLAY_ANSWER_DURATION_MILLIS)
    }

    private fun finish() {
        lessonTimer.cancel()
        findNavController().navigate(R.id.toSetPreferences)
    }

    private fun getRandomNote() = Note.values().random()

}

private const val DISPLAY_ANSWER_DURATION_MILLIS = 2000L
private const val LESSON_TIMER_TICK_LENGTH = 1000L
private const val NOTE_COUNTDOWN_TIMER_TICK_LENGTH = 10L