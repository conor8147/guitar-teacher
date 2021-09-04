package com.example.guitarteacher.di

import android.content.Context
import com.example.guitarteacher.LessonFragment
import com.example.guitarteacher.data.AppRepository
import com.example.guitarteacher.data.AppRepositoryImpl
import com.example.guitarteacher.domain.Fretboard
import com.example.guitarteacher.domain.StandardFretboard
import com.example.guitarteacher.utils.CountdownTimer
import com.example.guitarteacher.utils.Timer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
abstract class AppModule(private val context: Context) {

    @Binds
    abstract fun bindRepository(repository: AppRepositoryImpl): AppRepository

    @Binds
    abstract fun bindFretboard(fretboard: StandardFretboard): Fretboard

    @Binds
    abstract fun bindTimerFactory(factory: CountdownTimer.Factory): Timer.Factory
}