package com.kevin.storyappdicoding.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.StoryAppDicodingApplication
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.databinding.ActivityLoginBinding
import com.kevin.storyappdicoding.view.common.BaseActivity
import com.kevin.storyappdicoding.view.main.MainActivity
import com.kevin.storyappdicoding.view.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            register.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            btnLogin.setOnClickListener {
                val validationList = mutableListOf<Boolean>().apply {
                    add(loginEmail.validate(this@LoginActivity, getString(R.string.email)))
                    add(loginPassword.validate(this@LoginActivity, getString(R.string.password)))
                }
                if (validationList.all { it }) {
                    viewModel.loginUser(
                        loginEmail.editText?.text.toString(),
                        loginPassword.editText?.text.toString()
                    )
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.apply {
            loginResult.observe(this@LoginActivity) {
                when (it) {
                    is ApiResponse.Loading -> {
                        loadingDialog.show()
                    }
                    is ApiResponse.Success -> {
                        loadingDialog.dismiss()
                        preferencesHelper.user = it.data?.loginResult?.copy(
                            email = binding.loginEmail.editText?.text.toString(),
                        )
                        (application as StoryAppDicodingApplication).setTokenToHeader()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is ApiResponse.Error -> {
                        loadingDialog.dismiss()
                        binding.tvError.apply {
                            isVisible = true
                            text = it.errorMessage
                            Handler(Looper.getMainLooper()).postDelayed({
                                isVisible = false
                            }, 3000)
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = fade
        window.exitTransition = fade
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(binding.loginMessage, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.containerRegister, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.loginEmail, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.loginPassword, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500),
            )
            start()
        }
    }

    private fun TextInputLayout.validate(
        context: Context,
        title: String,
    ): Boolean {
        return when {
            editText?.text.isNullOrEmpty() -> {
                error = context.getString(R.string.is_empty, title)
                false
            }
            !error.isNullOrBlank() -> {
                false
            }
            else -> {
                error = null
                true
            }
        }
    }
}