package com.example.guitarteacher.utils

class TestTimer(
    private val totalTime: Long,
    private val tickLength: Long,
    private val onFinished: () -> Unit,
    private val onTick: (Long) -> Unit,
): Timer {
    private var time: Long = 0
    var paused = false
    var running = false
    var cancelled = false

    override fun start() {
        paused = false
        running = true
        cancelled = false
    }

    override fun pause() {
        paused = true
        running = false
        cancelled = false
    }

    override fun reset() {
        var paused = false
        var started = false
        var cancelled = false
    }

    override fun cancel() {
        paused = false
        running = false
        cancelled = true
    }

    override fun isFinished(): Boolean {
        return cancelled || time == totalTime
    }

    fun advanceTimeBy(millis: Long) {
        for (i in 0..millis step tickLength) {
            time += i
            if (time < totalTime) {
                onTick.invoke(time)
            } else {
                onFinished.invoke()
            }
        }
    }
}