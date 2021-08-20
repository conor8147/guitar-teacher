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
     * Called when the timer reaches 0.
     */
    fun onFinished()

    /**
     * Called at regular intervals while the timer is counting down. Tick length will not vary.
     */
    fun onTick(millisElapsed: Long)

    /**
     * Resets the timer to its base state. No information about it's previous state is retained.
     * If the timer has not left it's base state (ie start() has not been called since the timer was
     * constructed/reset, this will have no effect.
     */
    fun reset()

    /**
     * Cancel any processes currently running in this timer.
     */
    fun cancel()

}