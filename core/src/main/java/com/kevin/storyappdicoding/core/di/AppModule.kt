package com.kevin.storyappdicoding.core.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.kevin.storyappdicoding.core.BuildConfig
import com.kevin.storyappdicoding.core.data.model.RequestHeaders
import com.kevin.storyappdicoding.core.database.StoryDatabase
import com.kevin.storyappdicoding.core.utils.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(logging)
            }
        }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectionSpecs(spec)
            .addInterceptor(requestInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRequestHeader(): RequestHeaders {
        return RequestHeaders(language = "application/json")
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

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        StoryDatabase::class.java,
        "story.db"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideStoryDao(database: StoryDatabase) = database.storyDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(database: StoryDatabase) = database.remoteKeysDao()
}