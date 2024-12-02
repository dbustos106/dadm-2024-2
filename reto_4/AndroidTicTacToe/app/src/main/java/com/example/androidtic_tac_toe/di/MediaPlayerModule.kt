package com.example.androidtic_tac_toe.di

import android.content.Context
import com.example.androidtic_tac_toe.shared.MediaPlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaPlayerModule {

    @Provides
    @Singleton
    fun provideMediaPlayerManager(@ApplicationContext context: Context): MediaPlayerManager {
        return MediaPlayerManager(context)
    }
}
