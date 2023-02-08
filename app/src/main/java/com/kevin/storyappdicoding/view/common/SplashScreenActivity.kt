package com.kevin.storyappdicoding.view.common

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.transition.Fade
import com.kevin.storyappdicoding.databinding.ActivitySplashScreenBinding
import com.kevin.storyappdicoding.view.login.LoginActivity
import com.kevin.storyappdicoding.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val optionsCompat =
            ActivityOptionsCompat
                .makeSceneTransitionAnimation(
                    this,
                    binding.logo,
                    "logo_transition"
                )
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, if (preferencesHelper.isLogin) MainActivity::class.java else LoginActivity::class.java), optionsCompat.toBundle())
            finish()
        }, 2000)
    }
}