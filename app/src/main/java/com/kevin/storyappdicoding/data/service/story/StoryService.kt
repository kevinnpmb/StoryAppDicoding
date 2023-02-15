package com.kevin.storyappdicoding.data.service.story

import com.kevin.storyappdicoding.data.model.BaseResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryService {
    @GET("stories")
    suspend fun stories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null,
    ): Response<StoryResponse>

    @GET("stories/{id}")
    suspend fun storyDetail(
        @Path("id") id: String
    ): Response<StoryDetailResponse>

    @Multipart
    @POST("stories")
    suspend fun addNewStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<BaseResponse>

}