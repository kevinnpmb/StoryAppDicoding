package com.kevin.storyappdicoding.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
    }

    private fun playAnimation() {

    }
}