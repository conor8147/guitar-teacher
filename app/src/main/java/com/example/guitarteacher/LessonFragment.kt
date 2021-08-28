package com.example.guitarteacher

import android.content.Context
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
import com.example.guitarteacher.data.AppRepository
import com.example.guitarteacher.domain.Fretboard
import com.example.guitarteacher.utils.Timer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.properties.Delegates.notNull

@AndroidEntryPoint
class LessonFragment : Fragment(), LessonView {

    @Inject
    lateinit var fretboard: Fretboard
    @Inject
    lateinit var repository: AppRepository
    @Inject
    lateinit var timerFactory: Timer.Factory

    lateinit var remainingTimePb: ProgressBar
    lateinit var headingTv: TextView
    lateinit var mainTv: TextView
    lateinit var lessonTimeTv: TextView
    lateinit var pauseBtn: ImageButton

    lateinit var presenter: LessonPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = LessonPresenter(
            view = this,
            coroutineScope = lifecycleScope,
            fretboard = fretboard,
            repository = repository,
            timerFactory = timerFactory,
            applicationContext = context.applicationContext,
        )
    }

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

    override fun updateHeading(newHeading: String) {
        headingTv.text = newHeading
    }

    override fun updateMainText(text: String) {
        mainTv.text = text
    }

    override fun updateLessonTimer(text: String) {
        lessonTimeTv.text = text
    }

    override fun navigateUp() {
        findNavController().navigate(R.id.toSetPreferences)
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