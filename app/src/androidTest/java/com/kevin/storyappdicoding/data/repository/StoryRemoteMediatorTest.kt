package com.kevin.storyappdicoding.data.repository

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.service.story.StoryService
import com.kevin.storyappdicoding.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import com.kevin.storyappdicoding.database.StoryDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {
    private var mockApi: StoryService = FakeStoryService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, Story>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeStoryService : StoryService {
    override suspend fun stories(page: Int?, size: Int?, location: Int?): Response<StoryResponse> {
        val storyList = mutableListOf<Story>()
        for (i in 0..100) {
            val news = Story(
                "story-$i",
                "Story $i",
                "Description $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                SimpleDateFormat("yyyy-MM-dd").format(Date()),
            )
            storyList.add(news)
        }
        return Response.success(StoryResponse(false, "", storyList))
    }

    override suspend fun storyDetail(id: String): Response<StoryDetailResponse> {
        return Response.success(
            StoryDetailResponse(
                false, "", Story(
                    "story-1",
                    "Story 1",
                    "Description 1",
                    "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                    SimpleDateFormat("yyyy-MM-dd").format(Date()),
                )
            )
        )
    }

    override suspend fun addNewStories(
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): Response<BaseResponse> {
        return Response.success(BaseResponse(false, ""))
    }
}