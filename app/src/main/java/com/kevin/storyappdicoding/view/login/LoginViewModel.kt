package com.kevin.storyappdicoding.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.repository.AuthRepository
import com.kevin.storyappdicoding.data.service.auth.response.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    val loginResult = MutableLiveData<ApiResponse<AuthResponse>>()
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            authRepository.loginUser(email, password).flowOn(Dispatchers.IO).collect {
                loginResult.postValue(it)
            }
        }
    }
}