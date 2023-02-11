package com.kevin.storyappdicoding.view.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    val registerResult = MutableLiveData<ApiResponse<BaseResponse>>()
    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.registerUser(name, email, password).flowOn(Dispatchers.IO).collect {
                registerResult.postValue(it)
            }
        }
    }
}