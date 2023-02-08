package com.kevin.storyappdicoding.di

import com.google.gson.Gson
import com.kevin.storyappdicoding.BuildConfig.BASE_URL
import com.kevin.storyappdicoding.data.model.RequestHeaders
import com.kevin.storyappdicoding.data.service.auth.AuthService
import com.kevin.storyappdicoding.utils.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideRequestHeader(): RequestHeaders {
        return RequestHeaders(language = "application/json")
    }

    @Provides
    @Singleton
    fun provideAuthService(client: OkHttpClient): AuthService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestInterceptor(requestHeaders: RequestHeaders): RequestInterceptor {
        return RequestInterceptor(requestHeaders)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}