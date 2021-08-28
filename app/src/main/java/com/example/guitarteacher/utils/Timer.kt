package com.example.guitarteacher.utils

/**
 * An asynchronous countdown timer. This should not be constructed directly but should instead use a TimerFactory.
 */
interface Timer {

    /**
     * Starts the timer running. If the timer is already running this function will have no effect.
     */
    fun start()

    /**
     * Temporarily stop the timer. The timer can resume its previous state when start is called
     * again.
     */
    fun pause()

    /**
     * Resets the timer to its base state. No information about it's previous state is retained.
     * If the timer has not left it's base state (ie start() has not been called since the timer was
     * constructed/reset, this will have no effect.
     */
    fun reset()

    /**
     * Cancel any processes currently running in this timer. Calling this when the timer is already cancelled will have no effect
     */
    fun cancel()

    fun isFinished(): Boolean

    /**
     * Used to construct a timer without needing to specify the implementation of that timer
     */
    interface Factory {
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
}