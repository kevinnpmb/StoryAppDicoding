package com.kevin.storyappdicoding.core.view.common

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    lateinit var baseActivity: BaseActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = requireActivity() as BaseActivity
    }
}