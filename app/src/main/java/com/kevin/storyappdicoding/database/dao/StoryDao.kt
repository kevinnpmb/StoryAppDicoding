package com.kevin.storyappdicoding.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevin.storyappdicoding.database.model.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<Story>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: Story): Long

    @Query("SELECT * FROM table_story")
    suspend fun getAllStory(): List<Story>

    @Query("SELECT * FROM table_story WHERE lat IS NOT NULL AND lon IS NOT NULL")
    suspend fun getAllStoryWithLocation(): List<Story>

    @Query("SELECT * FROM table_story WHERE id = :id")
    suspend fun getStory(id: Int): Story

    @Query("DELETE FROM table_story")
    suspend fun deleteAll()
}