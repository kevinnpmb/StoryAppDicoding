package com.kevin.storyappdicoding.core.data.service.story.response

import com.google.gson.annotations.SerializedName
import com.kevin.storyappdicoding.data.model.Story

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("listStory")
    val listStory: List<Story>,
)