package com.kevin.storyappdicoding.view.main.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevin.storyappdicoding.databinding.FragmentAccountBinding
import com.kevin.storyappdicoding.utils.PreferencesHelper
import com.kevin.storyappdicoding.view.common.BaseFragment
import com.kevin.storyappdicoding.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : BaseFragment() {
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = preferencesHelper.user
        binding.apply {
            userName.text = user?.name ?: "-"
            userEmail.text = user?.email ?: "-"
            btnLogout.setOnClickListener {
                preferencesHelper.user = null
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                baseActivity.finish()
            }
        }
    }
}