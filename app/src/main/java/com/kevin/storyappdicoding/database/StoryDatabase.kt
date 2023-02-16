package com.kevin.storyappdicoding.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.database.dao.StoryDao

@Database(
    entities = [Story::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
}