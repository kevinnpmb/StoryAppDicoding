package com.kevin.storyappdicoding

import android.app.Application
import android.content.Context
import com.kevin.storyappdicoding.data.model.RequestHeaders
import com.kevin.storyappdicoding.utils.PreferencesHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class StoryAppDicodingApplication : Application() {
    @Inject
    lateinit var requestHeaders: RequestHeaders

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate() {
        super.onCreate()
        initializeApplication()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    private fun initializeApplication() {
        //load the current access token into all requests
        preferencesHelper.user?.token?.let { token ->
            requestHeaders.token = token
        }
    }
}
