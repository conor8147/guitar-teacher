package com.example.guitarteacher

import com.example.guitarteacher.domain.Note.*
import com.example.guitarteacher.domain.Fretboard
import com.example.guitarteacher.domain.Note
import javax.inject.Inject

/**
 * Representation of standard tuned six string guitar. Ie open tuning of EADGBE.
 */
class StandardFretboard @Inject constructor() : Fretboard {
    private val strings = (1..6).toList()
    private val openNotes = listOf(E, B, G, D, A, E)
    /**
     * Map of String to Note for each string on the guitar.
     */
    private val tuning = strings.zip(openNotes).toMap()

    override fun getTuningOfString(string: Int): Note? = tuning[string]

    override fun getStrings(): List<Int> = tuning.keys.sorted()
}