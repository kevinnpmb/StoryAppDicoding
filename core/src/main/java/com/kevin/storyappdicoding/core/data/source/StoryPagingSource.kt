package com.kevin.storyappdicoding.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.service.story.StoryService
import org.json.JSONObject

class StoryPagingSource(private val storyService: StoryService) : PagingSource<Int, Story>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = storyService.stories(page, params.loadSize)
            if (response.code() == 200) {
                LoadResult.Page(
                    data = response.body()?.listStory.orEmpty(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (response.body()?.listStory.isNullOrEmpty()) null else page + 1
                )
            } else {
                val errorBody = JSONObject(response.errorBody()!!.string())
                throw Exception(errorBody.getString("message"))
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}