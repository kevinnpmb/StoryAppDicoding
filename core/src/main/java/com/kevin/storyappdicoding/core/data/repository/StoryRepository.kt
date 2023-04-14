package com.kevin.storyappdicoding.core.data.repository

import android.location.Location
import androidx.paging.*
import com.kevin.storyappdicoding.core.data.model.ApiResponse
import com.kevin.storyappdicoding.core.data.model.BaseResponse
import com.kevin.storyappdicoding.core.data.model.Story
import com.kevin.storyappdicoding.core.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.core.data.service.story.response.StoryResponse
import com.kevin.storyappdicoding.core.database.StoryDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(
    private val client: OkHttpClient,
    private val storyDatabase: StoryDatabase
) {
    suspend fun storiesWithLocation(): Flow<ApiResponse<StoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = com.kevin.storyappdicoding.core.data.ApiConfig.storyService(client).stories(location = 1)
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
                val response = com.kevin.storyappdicoding.core.data.ApiConfig.storyService(client).storyDetail(id)
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
            remoteMediator = StoryRemoteMediator(storyDatabase, com.kevin.storyappdicoding.core.data.ApiConfig.storyService(client)),
            pagingSourceFactory = {
//                StoryPagingSource(storyService)
                storyDatabase.storyDao().getAllStory()
            }
        ).flow
    }

    suspend fun addStory(
        file: File,
        description: String,
        location: Location? = null
    ): Flow<ApiResponse<BaseResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                var latMediaTyped: RequestBody? = null
                var lonMediaTyped: RequestBody? = null
                val descMediaTyped = description.toRequestBody("text/plain".toMediaType())
                location?.let {
                    latMediaTyped = it.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lonMediaTyped =
                        it.longitude.toString().toRequestBody("text/plain".toMediaType())
                }
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val response = com.kevin.storyappdicoding.core.data.ApiConfig.storyService(client)
                    .addNewStories(imageMultipart, descMediaTyped, latMediaTyped, lonMediaTyped)
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