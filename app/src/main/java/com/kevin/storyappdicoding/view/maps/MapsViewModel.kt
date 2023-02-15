package com.kevin.storyappdicoding.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.repository.StoryRepository
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {
    private val _storiesResult = MutableLiveData<ApiResponse<StoryResponse>>()
    val storiesResult: LiveData<ApiResponse<StoryResponse>> get() = _storiesResult

    fun getStories() {
        viewModelScope.launch {
            storyRepository.storiesWithLocation().flowOn(Dispatchers.IO).collect {
                _storiesResult.postValue(it)
            }
        }
    }
}