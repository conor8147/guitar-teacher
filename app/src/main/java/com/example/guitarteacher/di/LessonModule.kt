package com.example.guitarteacher.di

import androidx.fragment.app.Fragment
import com.example.guitarteacher.LessonContract
import com.example.guitarteacher.LessonFragment
import com.example.guitarteacher.LessonPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import kotlinx.coroutines.channels.Channel

@Module
@InstallIn(FragmentComponent::class)
abstract class LessonModule {

    @Binds
    abstract fun bindLessonView(fragment: LessonFragment): LessonContract.View

    @Binds
    abstract fun bindLessonPresenter(presenter: LessonPresenter): LessonContract.Presenter

    @Module
    @InstallIn(FragmentComponent::class)
    object LessonProvider {

        @Provides
        fun provideLessonFragment(frag: Fragment): LessonFragment {
            return frag as LessonFragment
        }

        @Provides
        fun provideChannel(): Channel<String> = Channel(Channel.CONFLATED)
    }

}