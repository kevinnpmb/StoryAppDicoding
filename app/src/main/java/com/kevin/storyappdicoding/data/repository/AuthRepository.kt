package com.kevin.storyappdicoding.data.repository

import android.util.Log
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.service.auth.AuthService
import com.kevin.storyappdicoding.data.service.auth.response.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authService: AuthService) {
    suspend fun loginUser(email: String, password: String): Flow<ApiResponse<AuthResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.loginUser(email, password)
                if (response.code() == 200) {
                    emit(ApiResponse.Success(response.body()))
                } else {
                    val errorBody = JSONObject(response.errorBody()!!.string())
                    emit(ApiResponse.Error(errorBody.getString("message")))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    suspend fun registerUser(name: String, email: String, password: String): Flow<ApiResponse<BaseResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.registerUser(name, email, password)
                if (response.code() == 201) {
                    emit(ApiResponse.Success(response.body()))
                } else if (response.code() == 400) {
                    emit(ApiResponse.Error(response.body()?.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }
}