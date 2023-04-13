package com.kevin.storyappdicoding.core.utils


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.kevin.storyappdicoding.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * General Preferences Helper class, used for storing preference values using the Preference API
 */
@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext context: Context) {

    @Inject
    lateinit var gson: Gson

    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE)

    var user: User?
        get() = pref.getString(PREF_KEY_USER, null)?.let { gson.fromJson(it, User::class.java) }
        set(user) = pref.edit().putString(PREF_KEY_USER, gson.toJson(user)).apply()

    val isLogin: Boolean get() = user?.token != null

    companion object {
        private const val PREF_BUFFER_PACKAGE_NAME = "com.kevin.storyappdicoding"
        private const val PREF_KEY_USER = "user"
    }
}
