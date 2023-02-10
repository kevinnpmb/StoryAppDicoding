package com.kevin.storyappdicoding.data.service.story

import com.kevin.storyappdicoding.data.service.story.response.StoryDetailResponse
import com.kevin.storyappdicoding.data.service.story.response.StoryResponse
import retrofit2.Response
import retrofit2.http.*

interface StoryService {
    @GET("stories")
    suspend fun stories(): Response<StoryResponse>

    @GET("stories/{id}")
    suspend fun storyDetail(
        @Path("id") id: String
    ): Response<StoryDetailResponse>
}