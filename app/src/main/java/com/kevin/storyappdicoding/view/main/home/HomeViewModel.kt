package com.kevin.storyappdicoding.view.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {
    private val _storiesResult = MutableLiveData<PagingData<Story>>()
    val storiesResult: LiveData<PagingData<Story>> get() = _storiesResult

    val taskListState = MutableLiveData(TaskListState.LOADING)

    fun getStories() {
        viewModelScope.launch {
            storyRepository.storiesForList().collectLatest {
                _storiesResult.postValue(it)
            }
        }
    }

    enum class TaskListState {
        LOADING,
        EMPTY,
        ERROR,
        PRESENT
    }
}