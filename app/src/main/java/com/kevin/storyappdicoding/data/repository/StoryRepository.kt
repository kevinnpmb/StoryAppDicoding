package com.kevin.storyappdicoding.data.repository

import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.service.auth.AuthService
import com.kevin.storyappdicoding.data.service.auth.response.AuthResponse
import com.kevin.storyappdicoding.data.service.story.StoryService
import com.kevin.storyappdicoding.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(private val storyService: StoryService) {
    suspend fun stories(): Flow<ApiResponse<StoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.stories()
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

    suspend fun storyDetail(id: String): Flow<ApiResponse<StoryDetailResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.storyDetail(id)
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

    suspend fun addStory(file: File, description: String): Flow<ApiResponse<BaseResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val descMediaTyped = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val response = storyService.addNewStories(imageMultipart, descMediaTyped)
                if (response.code() == 201) {
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
}