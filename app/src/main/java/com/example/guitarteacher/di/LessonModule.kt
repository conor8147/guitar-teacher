package com.example.guitarteacher.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.guitarteacher.LessonContract
import com.example.guitarteacher.LessonFragment
import com.example.guitarteacher.LessonPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(FragmentComponent::class)
abstract class LessonModule {

    @Binds
    abstract fun bindLessonView(fragment: LessonFragment): LessonContract.View

    @Binds
    abstract fun provideLessonPresenter(presenter: LessonPresenter): LessonContract.Presenter

    @Binds
    abstract fun provideApplicationContext(@ApplicationContext context: Context): Context
}

@Module
@InstallIn(FragmentComponent::class)
object LessonFragmentModule {

    @Provides
    fun provideLessonFragment(frag: Fragment): LessonFragment {
        return frag as LessonFragment
    }
}
