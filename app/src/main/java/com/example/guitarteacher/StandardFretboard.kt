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
    override val tuning = strings.zip(openNotes).toMap()

    override fun getFretForNoteOnString(note: Note, guitarString: Int): Int {
        val openNote = tuning[guitarString]
        val openNoteIndex = Note.values().indexOf(openNote)
        val goalNoteIndex = Note.values().indexOf(note)

        return Math.floorMod(
            (goalNoteIndex - openNoteIndex),
            Note.values().size
        )
    }
}