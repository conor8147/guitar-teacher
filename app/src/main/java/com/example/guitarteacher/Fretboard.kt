package com.example.guitarteacher

interface Fretboard {
    /**
     * Get open note for given string.
     * String numbering is standard guitar numbering. ie. 1st string is the thinnest string.
     * Returns null if the given string does not exist.
     */
    fun getTuningOfString(string: Int): Note?

    /**
     * Get Strings ordered from bottom (high E) to top.
     */
    fun getStrings(): List<Int>
}

