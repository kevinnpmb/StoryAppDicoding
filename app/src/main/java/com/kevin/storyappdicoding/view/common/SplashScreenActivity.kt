package com.kevin.storyappdicoding.view.common

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.transition.Fade
import com.kevin.storyappdicoding.databinding.ActivitySplashScreenBinding
import com.kevin.storyappdicoding.view.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
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
        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, LoginActivity::class.java), optionsCompat.toBundle())
//        }, 2000)
        binding.logo.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java), optionsCompat.toBundle())
        }
    }
}