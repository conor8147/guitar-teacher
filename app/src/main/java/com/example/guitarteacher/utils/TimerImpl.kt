package com.example.guitarteacher.utils

import javax.inject.Inject

/**
 * An implementation of a Timer.
 */
class TimerImpl private constructor(
    private val totalTime: Long,
    private val tickLength: Long,
    private val onFinished: () -> Unit,
    private val onTick: (Long) -> Unit,
) : Timer {
    override fun start() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun onFinished() {
        TODO("Not yet implemented")
    }

    override fun onTick(millisElapsed: Long) {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    /**
     * Set the constructor of the TimerImpl to be private and have a nested TimerFactory class here, so
     * that the only way to get a new instance of TimerImpl is by using TimerImpl.Factory.
     */
    class Factory @Inject constructor() : TimerFactory {
        override fun createTimer(
            totalTime: Long,
            tickLength: Long,
            onFinished: () -> Unit,
            onTick: (Long) -> Unit
        ): Timer {
            return TimerImpl(
                totalTime = totalTime,
                tickLength = tickLength,
                onFinished = onFinished,
                onTick = onTick,
            )
        }
    }
}