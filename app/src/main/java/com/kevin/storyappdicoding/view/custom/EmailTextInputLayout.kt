package com.kevin.storyappdicoding.view.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.utils.Utilities.dpToPx

class EmailTextInputLayout : TextInputLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun init() {
        hint = context.getString(R.string.enter_your, context.getString(R.string.email))
        boxBackgroundColor = ContextCompat.getColor(context, R.color.white)
        setBoxCornerRadii(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
        isExpandedHintEnabled = false

        // Edit Text
        editText?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        editText?.setPaddingRelative(12, 0, 12, 0)
        editText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val email = s.toString()
                error = when {
                    email.isBlank() -> context.getString(R.string.is_empty, context.getString(R.string.email))
                    isValidEmail(email) -> context.getString(R.string.email_not_valid)
                    else -> null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    fun isValidEmail(email: String): Boolean {
        val pattern = Regex(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        )
        return pattern.matches(email)
    }
}