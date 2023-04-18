package com.kevin.storyappdicoding.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.kevin.storyappdicoding.database.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        StoryDatabase::class.java,
        "story.db"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideCartDao(database: StoryDatabase) = database.storyDao()
}