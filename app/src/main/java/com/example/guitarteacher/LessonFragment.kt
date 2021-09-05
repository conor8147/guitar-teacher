package com.example.guitarteacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.guitarteacher.databinding.FragmentLessonBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class LessonFragment : Fragment(), LessonContract.View {

    @Inject
    lateinit var presenter: LessonPresenter

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        binding.pauseBtn.setOnClickListener {
            it.isSelected = !it.isSelected
            toggleTimersPaused(it)
        }
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.startLesson()
    }

    override fun onDestroyView() {
        presenter.endLesson()
        super.onDestroyView()
        _binding = null
    }

    override fun setProgressMax(millis: Long) {
        binding.remainingTimePb.max = millis.toInt()
    }

    override fun updateProgress(millisRemaining: Long) {
        binding.remainingTimePb.progress = millisRemaining.toInt()
    }

    override fun updateLessonTimer(text: String) {
        binding.lessonTimeTv.text = text
    }

    override fun navigateUp() {
        findNavController().navigate(R.id.toSetPreferences)
    }

    override fun getCoroutineScope(): CoroutineScope = lifecycleScope

    override fun displayNoteAndString(note: String, guitarString: Int) {
        binding.headingTv.text = "String: $guitarString"
        binding.mainTv.text = note
    }

    override fun displayAnswer(correctFret: Int) {
        binding.headingTv.text = requireContext().getString(R.string.fret)
        binding.mainTv.text = correctFret.toString()
    }

    private fun toggleTimersPaused(it: View) {
        if (it.isSelected) {
            presenter.pauseLesson()
        } else {
            presenter.resumeLesson()
        }
    }
}