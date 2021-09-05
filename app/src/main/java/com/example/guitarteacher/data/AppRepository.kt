package com.example.guitarteacher.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface AppRepository {

    /**
     * Get total time of a lesson in milliseconds.
     */
    fun getTotalTime(): Long

    /**
     * Set the total time for a lesson in milliseconds.
     */
    fun writeTotalTime(millis: Long)

    /**
     * Get time per note in milliseconds.
     */
    fun getTimePerNote(): Long

    /**
     * Set time per note in milliseconds.
     */
    fun writeTimePerNote(millis: Long)

    /**
     * Get guitar string the user will be tested on.
     * Strings are numbered from 1 (the thinnest string) upwards.
     */
    fun getGuitarString(): Int

    /**
     * Set guitar string to test the user on.
     * Strings are numbered from 1 (the thinnest string) upwards.
     */
    fun writeGuitarString(string: Int)
}

@Singleton
class AppRepositoryImpl @Inject constructor(
    @ApplicationContext context : Context
): AppRepository {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

    override fun getTotalTime(): Long = sharedPrefs.getLong(
        TOTAL_TIME_KEY,
        DEFAULT_LESSON_TIME_MILLIS,
    )

    override fun writeTotalTime(millis: Long) {
        with(sharedPrefs.edit()) {
            putLong(TOTAL_TIME_KEY, millis)
            apply()
        }
    }

    override fun getTimePerNote(): Long = sharedPrefs.getLong(
        ROUND_PERIOD_KEY,
        DEFAULT_ROUND_PERIOD_MILLIS
    )

    override fun writeTimePerNote(millis: Long) {
        with(sharedPrefs.edit()) {
            putLong(ROUND_PERIOD_KEY, millis)
            apply()
        }
    }

    override fun getGuitarString(): Int = sharedPrefs.getInt(
        GUITAR_STRING_KEY,
        DEFAULT_GUITAR_STRING
    )

    override fun writeGuitarString(string: Int) {
        with(sharedPrefs.edit()) {
            putInt(GUITAR_STRING_KEY, string)
            apply()
        }
    }
}

private const val TOTAL_TIME_KEY = "total time key"
private const val ROUND_PERIOD_KEY = "round period key"
private const val GUITAR_STRING_KEY = "guitar string key"

private const val DEFAULT_LESSON_TIME_MILLIS = 60_000L
private const val DEFAULT_ROUND_PERIOD_MILLIS = 5L
private const val DEFAULT_GUITAR_STRING = 6

private const val SHARED_PREFERENCES_FILE_KEY = "Shared preferences file key"
