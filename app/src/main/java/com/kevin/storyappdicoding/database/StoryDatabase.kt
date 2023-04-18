package com.kevin.storyappdicoding.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevin.storyappdicoding.database.model.Story
import com.kevin.storyappdicoding.database.dao.StoryDao
import com.kevin.storyappdicoding.database.dao.UserDao
import com.kevin.storyappdicoding.database.model.User

@Database(
    entities = [Story::class, User::class], version = 2, exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun userDao(): UserDao
}