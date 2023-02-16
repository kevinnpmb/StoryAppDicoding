package com.kevin.storyappdicoding.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.service.story.StoryService
import com.kevin.storyappdicoding.database.StoryDatabase

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: StoryService
) : RemoteMediator<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Story>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        return try {
            val responseData = apiService.stories(page, state.config.pageSize)
            val endOfPaginationReached = responseData.body()?.listStory.isNullOrEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                database.storyDao().insertStory(responseData.body()?.listStory.orEmpty())
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }
}