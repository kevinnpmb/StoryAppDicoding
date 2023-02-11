package com.kevin.storyappdicoding.view.main.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {
    var photoFile: File? = null
    val storyResult = MutableLiveData<ApiResponse<BaseResponse>>()
    fun addStory(description: String) {
        viewModelScope.launch {
            photoFile?.let { photoFile ->
                storyRepository.addStory(photoFile, description).flowOn(Dispatchers.IO).collect {
                    storyResult.postValue(it)
                }
            }
        }
    }
}