package com.example.guitarteacher.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface AppRepository {

    /**
     * Get total time of a lesson in seconds.
     */
    fun getTotalTime(): Int

    /**
     * Set the total time for a lesson in seconds.
     */
    fun writeTotalTime(seconds: Int)

    /**
     * Get time per note in seconds.
     */
    fun getTimePerNote(): Int

    /**
     * Set time per note in seconds.
     */
    fun writeTimePerNote(seconds: Int)

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

    override fun getTotalTime(): Int = sharedPrefs.getInt(
        TOTAL_TIME_KEY,
        DEFAULT_LESSON_TIME_SECONDS
    )

    override fun writeTotalTime(seconds: Int) {
        with(sharedPrefs.edit()) {
            putInt(TOTAL_TIME_KEY, seconds)
            apply()
        }
    }

    override fun getTimePerNote(): Int = sharedPrefs.getInt(
        ROUND_PERIOD_KEY,
        DEFAULT_ROUND_PERIOD_SECONDS
    )

    override fun writeTimePerNote(seconds: Int) {
        with(sharedPrefs.edit()) {
            putInt(ROUND_PERIOD_KEY, seconds)
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

private const val DEFAULT_LESSON_TIME_SECONDS = 600
private const val DEFAULT_ROUND_PERIOD_SECONDS = 5
private const val DEFAULT_GUITAR_STRING = 6

private const val SHARED_PREFERENCES_FILE_KEY = "Shared preferences file key"
