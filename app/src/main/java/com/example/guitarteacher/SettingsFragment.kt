package com.example.guitarteacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.guitarteacher.domain.Fretboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject lateinit var fretboard: Fretboard
    @Inject lateinit var repository: AppRepository
    private lateinit var timeEditText: EditText
    private lateinit var periodEditText: EditText
    private lateinit var stringSpinner: Spinner
    private lateinit var startBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false).apply {
        timeEditText = findViewById(R.id.timeEditText)
        periodEditText = findViewById(R.id.periodEditText)
        stringSpinner = findViewById(R.id.stringPicker)
        startBtn = findViewById(R.id.startBtn)
        bind()
    }

    private fun bind() {
        stringSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            fretboard.getStrings()
        )

        // Pre-fill fields with previous preferences
        timeEditText.setText(repository.getTotalTime().toString())
        periodEditText.setText(repository.getRoundPeriod().toString())
        stringSpinner.setSelection(repository.getGuitarString() - 1)

        startBtn.setOnClickListener {
            repository.writeTotalTime(timeEditText.text.toString().toInt())
            repository.writeRoundPeriod(periodEditText.text.toString().toInt())
            repository.writeGuitarString(stringSpinner.selectedItem as Int)

            findNavController().navigate(R.id.mainFragment)
        }
    }
}