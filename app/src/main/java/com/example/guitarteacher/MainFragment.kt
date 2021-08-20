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
import com.example.guitarteacher.domain.Fretboard
import com.example.guitarteacher.domain.Note
import com.example.guitarteacher.utils.Timer
import com.example.guitarteacher.utils.TimerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.properties.Delegates.notNull

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var fretboard: Fretboard

    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var timerFactory: TimerFactory

    lateinit var remainingTimePb: ProgressBar
    lateinit var fretAnswerTv: TextView
    lateinit var noteTv: TextView

    private var periodMillis by notNull<Long>()
    private var guitarString by notNull<Int>()

    private val jobs = mutableListOf<Job>()

    /** Timer for the entire lesson */
    private lateinit var lessonTimer: Timer

    override fun onAttach(context: Context) {
        super.onAttach(context)
        periodMillis = repository.getRoundPeriod() * 1000L // TODO get rid of these magic numbers, all time should be converted to millis before it is stored by the repo, and use millis everywhere internally.
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
    ): View? = inflater.inflate(R.layout.fragment_main, container, false).apply {
        remainingTimePb = findViewById(R.id.remainingTimePb)
        fretAnswerTv = findViewById(R.id.fretAnswerTv)
        noteTv = findViewById((R.id.noteTv))

        remainingTimePb.max = periodMillis.toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runLesson()
    }

    private fun runLesson() {
        lifecycleScope.launch {
            lessonTimer.start()
        }.let { jobs.add(it) }

        lifecycleScope.launch {
            startNoteLesson()
        }.let { jobs.add(it) }
    }

    private suspend fun startNoteLesson() {
        while (true) {
            val randomNote = getRandomNote()
            val noteCountdown = newNoteCountdown()
            remainingTimePb.progress = periodMillis.toInt()
            noteTv.text = randomNote.noteString
            fretAnswerTv.text = ""

            noteCountdown.start()
            delay(periodMillis)
            noteCountdown.cancel()
            displayAnswer(randomNote)
            delay(DISPLAY_ANSWER_DURATION_MILLIS)

        }
    }

    /** get a new timer to be used to countdown for each note. */
    private fun newNoteCountdown() = timerFactory.createTimer(
        totalTime = periodMillis,
        tickLength = NOTE_COUNTDOWN_TIMER_TICK_LENGTH,
        onTick = { millisRemaining ->
            remainingTimePb.progress = millisRemaining.toInt()
        },
        onFinished = {},
    )

    private fun displayAnswer(note: Note) {
        val correctFret = getFretForNoteOnString(note, guitarString)
        fretAnswerTv.text = correctFret.toString()
    }

    private fun finish() {
        jobs.forEach {
            it.cancel()
        }
    }

    private fun getFretForNoteOnString(goalNote: Note, guitarString: Int): Int {
        val openNote = fretboard.getTuningOfString(guitarString)
        val openNoteIndex = Note.values().indexOf(openNote)
        val goalNoteIndex = Note.values().indexOf(goalNote)

        return Math.floorMod(
            (goalNoteIndex - openNoteIndex),
            Note.values().size
        )
    }

    private fun getRandomNote() = Note.values().random()

}

private const val DISPLAY_ANSWER_DURATION_MILLIS = 2000L
private const val LESSON_TIMER_TICK_LENGTH = 1000L
private const val NOTE_COUNTDOWN_TIMER_TICK_LENGTH = 10L