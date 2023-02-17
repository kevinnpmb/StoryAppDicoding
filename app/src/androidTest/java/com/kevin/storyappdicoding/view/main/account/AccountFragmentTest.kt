package com.kevin.storyappdicoding.view.main.account

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.view.login.LoginActivity
import com.kevin.storyappdicoding.view.main.MainActivity
import org.junit.Test

class AccountFragmentTest {
    @Test
    fun account_Logout() {
        Intents.init()
        val bundle = Bundle()
        launchFragmentInMainActivity<AccountFragment>(bundle, R.style.Theme_StoryAppDicoding)
        onView(withId(R.id.btnLogout))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btnLogout)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }

    private inline fun <reified T : Fragment> launchFragmentInMainActivity(
        fragmentArgs: Bundle? = null,
        @StyleRes themeResId: Int = R.style.Theme_StoryAppDicoding,
        crossinline action: Fragment.() -> Unit = {}
    ) {
        val startActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                MainActivity::class.java
            )
        ).putExtra(
            "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
            themeResId
        )

        ActivityScenario.launch<MainActivity>(startActivityIntent).onActivity { activity ->
            val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                Preconditions.checkNotNull(T::class.java.classLoader) as ClassLoader,
                T::class.java.name
            )
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()

            fragment.action()
        }
    }
}