package com.kevin.storyappdicoding.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.data.model.Response
import com.kevin.storyappdicoding.databinding.ActivityRegisterBinding
import com.kevin.storyappdicoding.utils.Utilities.registerValidateIfEmpty
import com.kevin.storyappdicoding.utils.Utilities.validate
import com.kevin.storyappdicoding.view.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerName.registerValidateIfEmpty(this, getString(R.string.name))
        playAnimation()
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnRegister.setOnClickListener {
                val validationList = mutableListOf<Boolean>().apply {
                    add(registerName.validate(this@RegisterActivity, getString(R.string.name)))
                    add(registerEmail.validate(this@RegisterActivity, getString(R.string.email)))
                    add(
                        registerPassword.validate(
                            this@RegisterActivity,
                            getString(R.string.password)
                        )
                    )
                }
                if (validationList.all { it }) {
                    viewModel.registerUser(
                        registerName.editText?.text.toString(),
                        registerEmail.editText?.text.toString(),
                        registerPassword.editText?.text.toString()
                    )
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.apply {
            registerResult.observe(this@RegisterActivity) {
                when (it) {
                    is Response.Loading -> {
                        loadingDialog.show()
                    }
                    is Response.Success -> {
                        loadingDialog.dismiss()
                        Toast.makeText(this@RegisterActivity, getString(R.string.register_success), Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }
                    is Response.Error -> {
                        loadingDialog.dismiss()
                        Toast.makeText(this@RegisterActivity, it.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(binding.registerMessage, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.registerName, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.registerEmail, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.registerPassword, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500),
            )
            start()
        }
    }
}