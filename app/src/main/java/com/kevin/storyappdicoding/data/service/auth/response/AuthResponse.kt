package com.kevin.storyappdicoding.data.service.auth.response

import com.google.gson.annotations.SerializedName
import com.kevin.storyappdicoding.data.model.User

data class AuthResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("loginResult")
    val loginResult: User
)