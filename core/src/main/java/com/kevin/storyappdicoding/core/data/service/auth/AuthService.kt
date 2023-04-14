package com.kevin.storyappdicoding.core.data.service.auth

import com.kevin.storyappdicoding.core.data.model.BaseResponse
import com.kevin.storyappdicoding.core.data.service.auth.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
    ): Response<BaseResponse>
}