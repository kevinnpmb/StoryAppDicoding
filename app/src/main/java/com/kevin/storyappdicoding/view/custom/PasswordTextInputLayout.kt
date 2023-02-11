package com.kevin.storyappdicoding.view.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.utils.Utilities.dpToPx


class PasswordTextInputLayout : TextInputLayout {
    lateinit var mEditText: TextInputEditText

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
        hint = context.getString(R.string.enter_your_password)
        boxBackgroundColor = ContextCompat.getColor(context, R.color.white)
        boxBackgroundMode = BOX_BACKGROUND_OUTLINE
        setBoxCornerRadii(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
        isExpandedHintEnabled = false
        setWillNotDraw(false)
        // Edit Text
        mEditText = TextInputEditText(context)
        mEditText.transformationMethod = PasswordTransformationMethod()
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        mEditText.layoutParams = layoutParams
        mEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        mEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password = s.toString()
                error = when {
                    password.isBlank() -> context.getString(
                        R.string.is_empty,
                        context.getString(R.string.password)
                    )
                    !isValidPassword(password) -> context.getString(R.string.password_less_than_eight)
                    else -> null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
        addView(mEditText)
    }

    fun isValidPassword(password: String) = password.length >= 8
}