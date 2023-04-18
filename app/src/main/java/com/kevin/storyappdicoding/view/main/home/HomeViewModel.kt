package com.kevin.storyappdicoding.view.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.kevin.storyappdicoding.data.model.Response
import com.kevin.storyappdicoding.database.model.Story
import com.kevin.storyappdicoding.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {
    private val _storiesResult = MutableLiveData<Response<List<Story>>>()
    val storiesResult: LiveData<Response<List<Story>>> get() = _storiesResult

    fun getStories() {
        viewModelScope.launch {
            storyRepository.storiesForList().collectLatest {
                _storiesResult.postValue(it)
            }
        }
    }
}