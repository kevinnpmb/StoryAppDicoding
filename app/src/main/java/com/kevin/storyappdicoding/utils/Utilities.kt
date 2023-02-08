package com.kevin.storyappdicoding.utils

import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R

object Utilities {
    val Number.dpToPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )

    fun TextInputLayout.validate(
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

    fun TextInputLayout.registerValidateIfEmpty(context: Context, title: String) {
        val clearTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error = if (p0.toString().isBlank()) context.getString(R.string.is_empty, title) else null
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        editText?.addTextChangedListener(clearTextChangedListener)
    }
}