package com.kevin.storyappdicoding.core.view.common

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kevin.storyappdicoding.core.R
import com.kevin.storyappdicoding.core.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    lateinit var loadingDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = AlertDialog.Builder(this, R.style.BackgroundTransparentDialog).run {
            setView(layoutInflater.inflate(R.layout.loading, null))
            setCancelable(false)
            create()
        }
    }
}