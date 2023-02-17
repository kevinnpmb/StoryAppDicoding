package com.kevin.storyappdicoding.view.login

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.data.ApiConfig
import com.kevin.storyappdicoding.utils.EspressoIdlingResource
import com.kevin.storyappdicoding.utils.JsonConverter
import com.kevin.storyappdicoding.utils.Utilities.getResourceString
import com.kevin.storyappdicoding.utils.Utilities.hasTextInputLayoutErrorText
import com.kevin.storyappdicoding.view.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)
    private val mockWebServer = MockWebServer()
    private var decorView: View? = null

    @Before
    fun setUp() {
        activity.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginUsers_Success() {
        Intents.init()
        val dummyEmail = "asdf@gmail.com"
        val dummyPassword = "asdfasdf"
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginEmail)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginPassword)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("login/login_success.json"))
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.btnLogin)).perform(click())
        intended(hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun loginUsers_EmptyEmail() {
        val dummyEmail = ""
        val dummyPassword = "asdfasdf"
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginEmail)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginPassword)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).perform(click())
        onView(withId(R.id.loginEmail)).check(
            matches(
                hasTextInputLayoutErrorText(
                    getResourceString(R.string.is_empty, getResourceString(R.string.email))
                )
            )
        )
    }

    @Test
    fun loginUsers_EmptyPassword() {
        val dummyEmail = "asdf@gmail.com"
        val dummyPassword = ""
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginEmail)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginPassword)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).perform(click())
        onView(withId(R.id.loginPassword)).check(
            matches(
                hasTextInputLayoutErrorText(
                    getResourceString(R.string.is_empty, getResourceString(R.string.password))
                )
            )
        )
    }

    @Test
    fun loginUsers_UserNotFound() {
        val dummyEmail = "fdas11991@mai.co"
        val dummyPassword = "asdfasdf"
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginEmail)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginPassword)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody(JsonConverter.readStringFromFile("login/login_user_not_found.json"))
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.btnLogin)).perform(click())
        onView(withText("User not found"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUsers_WrongPassword() {
        val dummyEmail = "asdf@gmail.com"
        val dummyPassword = "asdfasdfasdf"
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginEmail)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(
            allOf(
                isDescendantOfA(withId(R.id.loginPassword)),
                withClassName(endsWith("EditText"))
            )
        ).perform(typeText(dummyPassword), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody(JsonConverter.readStringFromFile("login/login_wrong_password.json"))
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.btnLogin)).perform(click())
        onView(withText("Invalid password"))
            .check(matches(isDisplayed()))
    }
}
