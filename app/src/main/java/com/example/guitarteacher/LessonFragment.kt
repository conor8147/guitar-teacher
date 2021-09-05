package com.example.guitarteacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class LessonFragment : Fragment(), LessonContract.View {

    @Inject lateinit var presenter: LessonPresenter

    private lateinit var remainingTimePb: ProgressBar
    private lateinit var headingTv: TextView
    private lateinit var mainTv: TextView
    private lateinit var lessonTimeTv: TextView
    private lateinit var pauseBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lesson, container, false).apply {
        bind()
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.startLesson()
    }

    override fun onDestroyView() {
        presenter.endLesson()
        super.onDestroyView()
    }

    override fun setProgressMax(millis: Long) {
        remainingTimePb.max = millis.toInt()
    }

    override fun updateProgress(millisRemaining: Long) {
        remainingTimePb.progress = millisRemaining.toInt()
    }

    override fun updateLessonTimer(text: String) {
        lessonTimeTv.text = text
    }

    override fun navigateUp() {
        findNavController().navigate(R.id.toSetPreferences)
    }

    override fun getCoroutineScope(): CoroutineScope = lifecycleScope

    override fun displayNoteAndString(note: String, guitarString: Int) {
        headingTv.text = "String: $guitarString"
        mainTv.text = note
    }

    override fun displayAnswer(correctFret: Int) {
        headingTv.text = requireContext().getString(R.string.fret)
        mainTv.text = correctFret.toString()
    }

    private fun View.bind() {
        remainingTimePb = findViewById(R.id.remainingTimePb)
        mainTv = findViewById(R.id.mainTv)
        lessonTimeTv = findViewById(R.id.lessonTimeTv)
        headingTv = findViewById(R.id.headingTv)
        pauseBtn = findViewById(R.id.pauseBtn)

        pauseBtn.setOnClickListener {
            it.isSelected = !it.isSelected
            toggleTimersPaused(it)
        }
    }

    private fun toggleTimersPaused(it: View) {
        if (it.isSelected) {
            presenter.pauseLesson()
        } else {
            presenter.resumeLesson()
        }
    }
}