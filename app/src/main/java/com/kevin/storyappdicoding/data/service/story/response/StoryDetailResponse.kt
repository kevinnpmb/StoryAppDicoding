package com.kevin.storyappdicoding.data.service.story.response

import com.google.gson.annotations.SerializedName
import com.kevin.storyappdicoding.data.model.Story

data class StoryDetailResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("story")
    val story: Story,
)