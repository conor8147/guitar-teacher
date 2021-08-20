package com.example.guitarteacher.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * My initial janky ass attempt at a timer.
 * TODO: come up with a better timer to replace this
 */
class CrappyTimer private constructor(
    private val totalTime: Long,
    private val tickLength: Long,
    private val onFinished: () -> Unit,
    private val onTick: (Long) -> Unit,
) : Timer {

    private val jobs = mutableListOf<Job>()

    override fun start() {
        GlobalScope.launch {
            startCountdown()
        }.let { jobs.add(it) }
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun onFinished() {
        onFinished.invoke()
    }

    override fun onTick(millisElapsed: Long) {
        onTick.invoke(millisElapsed)
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        jobs.forEach { it.cancel() }
    }

    private suspend fun startCountdown() {
        for (millisElapsed in 0..totalTime step tickLength) {
            onTick(millisElapsed)
            delay(tickLength)
        }
        onFinished()
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
            return CrappyTimer(
                totalTime = totalTime,
                tickLength = tickLength,
                onFinished = onFinished,
                onTick = onTick,
            )
        }
    }
}