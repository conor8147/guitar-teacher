package com.example.guitarteacher

import com.example.guitarteacher.Note.*
import javax.inject.Inject

/**
 * Representation of standard tuned six string guitar. Ie open tuning of EADGBE.
 */
class StandardFretboard @Inject constructor() : Fretboard {
    /**
     * Map of String to Note for each string on the guitar.
     */
    private val tuning = (1..6).toList()
        .zip(listOf(E, B, G, D, A, E))
        .toMap()

    override fun getTuningOfString(string: Int): Note? = tuning[string]

    override fun getStrings(): List<Int> = tuning.keys.sorted()
}