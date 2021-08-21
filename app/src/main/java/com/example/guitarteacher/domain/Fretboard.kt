package com.example.guitarteacher.domain

/**
 * Representation of a fretboard.
 */
interface Fretboard {

    /**
     * The open tuning of this fretboard. Map of each string to its tuning
     * Strings are numbered starting from 1. 1st string is the bottom string on the guitar when held the
     * conventional way.
     */
    val tuning: Map<Int, Note>

    /**
     * When given a string number and a note, returns the fret of the note on that string.
     */
    fun getFretForNoteOnString(note: Note, guitarString: Int): Int
}

