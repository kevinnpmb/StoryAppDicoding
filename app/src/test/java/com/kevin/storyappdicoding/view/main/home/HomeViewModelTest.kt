package com.kevin.storyappdicoding.view.main.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.repository.StoryRepository
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import com.kevin.storyappdicoding.data.source.StoryPagingSource
import com.kevin.storyappdicoding.utils.DataDummy
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest