package com.example.guitarteacher

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
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

    lateinit var remainingTimePb: ProgressBar
    lateinit var fretAnswerTv: TextView
    lateinit var noteTv: TextView

    private var periodMillis by notNull<Long>()
    private var lessonTimeMillis by notNull<Long>()
    private var string by notNull<Int>()

    private val jobs = mutableListOf<Job>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        periodMillis = repository.getRoundPeriod() * 1000L
        lessonTimeMillis = repository.getTotalTime() * 1000L
        string = repository.getGuitarString()
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
            startCountdown(
                time = lessonTimeMillis,
                onFinished = ::finish
            )
        }.let { jobs.add(it) }

        lifecycleScope.launch {
            startNoteLesson()
        }.let { jobs.add(it) }
    }

    private suspend fun startNoteLesson() {
        while (true) {
            val randomNote = getRandomNote()
            remainingTimePb.progress = periodMillis.toInt()
            noteTv.text = randomNote.noteString
            fretAnswerTv.text = ""

            startCountdown(
                time = periodMillis,
                onTick = { millisElapsed: Long ->
                    remainingTimePb.progress = (periodMillis - millisElapsed).toInt()
                },
                onFinished = { displayAnswer(randomNote) }
            )
            delay(DISPLAY_ANSWER_DURATION_MILLIS)
        }
    }

    private suspend fun startCountdown(
        time: Long,
        onTick: suspend (Long) -> Unit = {},
        onFinished: () -> Unit = {},
    ) {
        val tickLength = 10L
        for (millisElapsed in 0..time step tickLength) {
            lifecycleScope.launch { onTick(millisElapsed) }
            delay(tickLength)
        }
        onFinished()
    }

    private fun displayAnswer(note: Note) {
        val correctFret = getFretForNoteOnString(note, string)
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