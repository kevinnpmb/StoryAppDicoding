package com.kevin.storyappdicoding.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.database.dao.RemoteKeysDao
import com.kevin.storyappdicoding.database.dao.StoryDao
import com.kevin.storyappdicoding.database.model.RemoteKeys

@Database(
    entities = [Story::class, RemoteKeys::class], version = 1, exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}