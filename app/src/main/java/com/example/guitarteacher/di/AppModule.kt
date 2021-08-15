package com.example.guitarteacher.di

import android.content.Context
import com.example.guitarteacher.AppRepository
import com.example.guitarteacher.AppRepositoryImpl
import com.example.guitarteacher.Fretboard
import com.example.guitarteacher.StandardFretboard
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
}