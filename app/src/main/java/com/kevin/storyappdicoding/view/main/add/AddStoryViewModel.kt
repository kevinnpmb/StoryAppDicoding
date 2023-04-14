package com.kevin.storyappdicoding.view.main.add

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.storyappdicoding.core.data.model.ApiResponse
import com.kevin.storyappdicoding.core.data.model.BaseResponse
import com.kevin.storyappdicoding.core.data.repository.StoryRepository
import com.kevin.storyappdicoding.core.utils.Utilities
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
    var location: Location? = null
    var isBackCamera: Boolean? = null
    val isShowLocation = MutableLiveData(false)
    val storyResult = MutableLiveData<ApiResponse<BaseResponse>>()
    fun addStory(description: String) {
        viewModelScope.launch {
            photoFile?.let { photoFile ->
                isBackCamera?.let { isBackCamera ->
                    Utilities.reduceFileImageCameraX(photoFile, isBackCamera)
                } ?: run {
                    Utilities.reduceFileImage(photoFile)
                }
                storyRepository.addStory(photoFile, description, location).flowOn(Dispatchers.IO)
                    .collect {
                        storyResult.postValue(it)
                    }
            }
        }
    }
}