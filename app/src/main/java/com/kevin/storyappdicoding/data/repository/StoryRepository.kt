package com.kevin.storyappdicoding.data.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.data.service.story.StoryService
import com.kevin.storyappdicoding.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import com.kevin.storyappdicoding.data.source.StoryPagingSource
import com.kevin.storyappdicoding.database.StoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(private val storyService: StoryService, private val storyDatabase: StoryDatabase) {
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

    fun storiesForList(): Flow<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, storyService),
            pagingSourceFactory = {
//                StoryPagingSource(storyService)
                storyDatabase.storyDao().getAllStory()
            }
        ).flow
    }

    suspend fun addStory(file: File, description: String, location: Location? = null): Flow<ApiResponse<BaseResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                var latMediaTyped: RequestBody? = null
                var lonMediaTyped: RequestBody? = null
                val descMediaTyped = description.toRequestBody("text/plain".toMediaType())
                location?.let {
                    latMediaTyped = it.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lonMediaTyped = it.longitude.toString().toRequestBody("text/plain".toMediaType())
                }
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val response = storyService.addNewStories(imageMultipart, descMediaTyped, latMediaTyped, lonMediaTyped)
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