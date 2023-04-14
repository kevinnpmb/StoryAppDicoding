package com.kevin.storyappdicoding.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevin.storyappdicoding.core.data.model.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<Story>)

    @Query("SELECT * FROM table_story")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("DELETE FROM table_story")
    suspend fun deleteAll()
}