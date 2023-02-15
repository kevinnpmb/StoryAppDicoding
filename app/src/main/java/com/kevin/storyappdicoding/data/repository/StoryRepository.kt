package com.kevin.storyappdicoding.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.service.story.StoryService
import com.kevin.storyappdicoding.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import com.kevin.storyappdicoding.data.source.StoryPagingSource
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
    suspend fun storiesWithLocation(): Flow<ApiResponse<StoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.stories(location = 1)
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

    fun storiesForList(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(storyService)
            }
        ).flow
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