package com.kevin.storyappdicoding.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.transition.Fade
import com.kevin.storyappdicoding.databinding.ActivityLoginBinding
import com.kevin.storyappdicoding.view.common.SplashScreenActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.logo.setOnClickListener {
            startActivity(Intent(this, SplashScreenActivity::class.java), optionsCompat.toBundle())
        }
        playAnimation()
    }

    private fun playAnimation() {

    }
}