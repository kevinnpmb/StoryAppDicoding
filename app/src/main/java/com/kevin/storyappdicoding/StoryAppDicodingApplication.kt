package com.kevin.storyappdicoding

import android.app.Application
import android.content.Context
import com.kevin.storyappdicoding.utils.PreferencesHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class StoryAppDicodingApplication : Application() {
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
}
