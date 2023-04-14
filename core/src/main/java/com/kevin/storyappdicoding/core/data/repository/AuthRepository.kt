package com.kevin.storyappdicoding.core.data.repository

import com.kevin.storyappdicoding.core.data.model.ApiResponse
import com.kevin.storyappdicoding.core.data.model.BaseResponse
import com.kevin.storyappdicoding.core.data.service.auth.response.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val client: OkHttpClient) {
    suspend fun loginUser(email: String, password: String): Flow<ApiResponse<AuthResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = com.kevin.storyappdicoding.core.data.ApiConfig.authService(client).loginUser(email, password)
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

    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Flow<ApiResponse<BaseResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = com.kevin.storyappdicoding.core.data.ApiConfig.authService(client).registerUser(name, email, password)
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