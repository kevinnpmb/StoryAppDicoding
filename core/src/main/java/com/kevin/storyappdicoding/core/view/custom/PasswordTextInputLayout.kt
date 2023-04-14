package com.kevin.storyappdicoding.core.view.custom

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.core.R


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
        hintTextColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(Color.BLACK)
        )
        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
        setBoxCornerRadii(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
        setWillNotDraw(false)
        // Edit Text
        mEditText = TextInputEditText(context).apply {
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            this.layoutParams = layoutParams
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            typeface = ResourcesCompat.getFont(context, R.font.poppins_font)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Do nothing.
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val password = s.toString()
                    this@PasswordTextInputLayout.error = when {
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
        }
        addView(mEditText)
    }

    fun isValidPassword(password: String) = password.length >= 8
    private val Number.dpToPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
}