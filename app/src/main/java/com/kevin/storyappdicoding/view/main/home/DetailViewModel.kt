package com.kevin.storyappdicoding.view.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.repository.StoryRepository
import com.kevin.storyappdicoding.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val storyRepository: StoryRepository) : ViewModel() {
    val storyResult = MutableLiveData<ApiResponse<StoryDetailResponse>>()
    fun getStory(id: String) {
        viewModelScope.launch {
            storyRepository.storyDetail(id).flowOn(Dispatchers.IO).collect {
                storyResult.postValue(it)
            }
        }
    }
}