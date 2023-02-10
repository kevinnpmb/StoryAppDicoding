package com.kevin.storyappdicoding.view.main.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.repository.StoryRepository
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storyRepository: StoryRepository) : ViewModel() {
    val storiesResult = MutableLiveData<ApiResponse<StoryResponse>>()
    fun getStories() {
        viewModelScope.launch {
            storyRepository.stories().flowOn(Dispatchers.IO).collect {
                storiesResult.postValue(it)
            }
        }
    }
}