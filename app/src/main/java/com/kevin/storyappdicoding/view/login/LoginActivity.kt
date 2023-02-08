package com.kevin.storyappdicoding.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.activity.viewModels
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.data.model.ApiResponse
import com.kevin.storyappdicoding.databinding.ActivityLoginBinding
import com.kevin.storyappdicoding.utils.Utilities.registerValidateIfEmpty
import com.kevin.storyappdicoding.utils.Utilities.validate
import com.kevin.storyappdicoding.view.common.BaseActivity
import com.kevin.storyappdicoding.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginPassword.registerValidateIfEmpty(this, getString(R.string.password))
        playAnimation()
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                val validationList = mutableListOf<Boolean>().apply {
                    add(loginEmail.validate(this@LoginActivity, getString(R.string.email)))
                    add(loginPassword.validate(this@LoginActivity, getString(R.string.password)))
                }
                if (validationList.all { it }) {
                    viewModel.loginUser(loginEmail.editText?.text.toString(), loginPassword.editText?.text.toString())
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
                        loadingDialog.show()
                        preferencesHelper.user = it.data?.loginResult
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is ApiResponse.Error -> {
                        loadingDialog.show()
                        Toast.makeText(this@LoginActivity, it.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
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
}