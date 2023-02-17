package com.kevin.storyappdicoding.view.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.kevin.storyappdicoding.R
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.kevin.storyappdicoding.BuildConfig
import com.kevin.storyappdicoding.utils.ApiConfig
import com.kevin.storyappdicoding.utils.EspressoIdlingResource
import com.kevin.storyappdicoding.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)
    private val mockWebServer = MockWebServer()
    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginUsers_Success() {
        val dummyEmail = "asdf@gmail.com"
        val dummyPassword = "asdfasdf"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.loginEmail)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.loginPassword)).perform(typeText(dummyPassword), closeSoftKeyboard())
    }
}