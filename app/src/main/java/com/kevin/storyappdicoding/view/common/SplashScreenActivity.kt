package com.kevin.storyappdicoding.view.common

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityOptionsCompat
import com.kevin.storyappdicoding.databinding.ActivitySplashScreenBinding
import com.kevin.storyappdicoding.core.view.common.BaseActivity
import com.kevin.storyappdicoding.login.LoginActivity
import com.kevin.storyappdicoding.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private var mShouldFinish = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val optionsCompat =
                ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                        this,
                        binding.logo,
                        "logo_transition"
                    )
            startActivity(
                Intent(
                    this,
                    if (preferencesHelper.isLogin) MainActivity::class.java else com.kevin.storyappdicoding.login.LoginActivity::class.java
                ), optionsCompat.toBundle()
            )
            mShouldFinish = true
        }, 2000)
    }

    override fun onStop() {
        super.onStop()
        if (mShouldFinish)
            finish()
    }
}