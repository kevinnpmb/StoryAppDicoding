package com.kevin.storyappdicoding.data

import com.kevin.storyappdicoding.data.service.auth.AuthService
import com.kevin.storyappdicoding.data.service.story.StoryService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    var BASE_URL = "https://story-api.dicoding.dev/v1/"

    private fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun authService(client: OkHttpClient): AuthService =
        retrofit(client).create(AuthService::class.java)

    fun storyService(client: OkHttpClient): StoryService =
        retrofit(client).create(StoryService::class.java)
}