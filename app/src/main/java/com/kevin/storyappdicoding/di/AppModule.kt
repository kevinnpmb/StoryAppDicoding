package com.kevin.storyappdicoding.di

import com.google.gson.Gson
import com.kevin.storyappdicoding.BuildConfig.BASE_URL
import com.kevin.storyappdicoding.data.model.RequestHeaders
import com.kevin.storyappdicoding.data.service.auth.AuthService
import com.kevin.storyappdicoding.data.service.story.StoryService
import com.kevin.storyappdicoding.utils.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
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
    fun providesOkHttpClient(
        logging: HttpLoggingInterceptor,
        requestInterceptor: RequestInterceptor
    ): OkHttpClient {
        var cipherSuites: MutableList<CipherSuite>? =
            ConnectionSpec.MODERN_TLS.cipherSuites as MutableList<CipherSuite>?
        if (!cipherSuites!!.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
            cipherSuites = ArrayList(cipherSuites)
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
        }
        val spec = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectionSpecs(spec)
            .addInterceptor(logging)
            .addInterceptor(requestInterceptor)
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
    fun provideStoryService(client: OkHttpClient): StoryService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestInterceptor(requestHeaders: RequestHeaders): RequestInterceptor {
        return RequestInterceptor(requestHeaders)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}