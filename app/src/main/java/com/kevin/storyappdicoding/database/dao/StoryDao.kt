package com.kevin.storyappdicoding.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevin.storyappdicoding.data.model.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<Story>)

    @Query("SELECT * FROM quote")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("DELETE FROM quote")
    suspend fun deleteAll()
}