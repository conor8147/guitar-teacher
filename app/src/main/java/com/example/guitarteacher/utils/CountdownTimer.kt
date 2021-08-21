package com.example.guitarteacher.utils

import android.os.CountDownTimer
import javax.inject.Inject

/**
 * An implementation of a Timer.
 */
class CountdownTimer private constructor(
    private val totalTime: Long,
    private val tickLength: Long,
    private val onFinished: () -> Unit,
    private val onTick: (Long) -> Unit,
) : Timer {

    private var timer: CountDownTimer? = null
        set(newTimer) {
            timer?.cancel()
            field = newTimer
        }

    override fun start() {
        if (timer == null) {
            timer = object : CountDownTimer(totalTime, tickLength) {
                override fun onTick(millisUntilFinished: Long) {
                    onTick.invoke(millisUntilFinished)
                }

                override fun onFinish() {
                    onFinished()
                }
            }.apply { start() }
        }
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun onFinished() {
        onFinished.invoke()
        reset()
    }

    override fun onTick(millisElapsed: Long) {
        onTick.invoke(millisElapsed)
    }

    // TODO: investigate if there's any point in this
    override fun reset() {
        cancel()
    }

    override fun cancel() {
        timer?.cancel()
        timer = null
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
            onTick: (Long) -> Unit,
        ): Timer {
            return CountdownTimer(
                totalTime = totalTime,
                tickLength = tickLength,
                onFinished = onFinished,
                onTick = onTick,
            )
        }
    }
}