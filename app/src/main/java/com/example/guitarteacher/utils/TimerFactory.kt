package com.example.guitarteacher.utils

/**
 * Used to construct a timer without needing to specify the implementation of that timer
 */
interface TimerFactory {
    /**
     * Create the timer.
     *
     * @param totalTime: The total duration of the timer in milliseconds.
     * @param tickLength: The duration between ticks in milliseconds.
     * @param onFinished: function that will be executed when the timer finishes.
     * @param onTick: function that will be executed when the timer finishes.
     */
    fun createTimer(
        totalTime: Long,
        tickLength: Long,
        onFinished: () -> Unit,
        onTick: (Long) -> Unit,
    ): Timer
}