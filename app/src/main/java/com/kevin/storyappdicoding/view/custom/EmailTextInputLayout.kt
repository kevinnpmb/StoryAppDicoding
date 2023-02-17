package com.kevin.storyappdicoding.view.custom

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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R


class EmailTextInputLayout : TextInputLayout {
    lateinit var mEditText: TextInputEditText

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun init() {
        hint = context.getString(R.string.enter_your_email)
        hintTextColor = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.BLACK)
        )
        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
        setBoxCornerRadii(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
        setWillNotDraw(false)
        // Edit Text
        mEditText = TextInputEditText(context).apply {
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            this.layoutParams = layoutParams
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                    // Do nothing.
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val email = s.toString()
                    this@EmailTextInputLayout.error = when {
                        email.isBlank() -> context.getString(
                            R.string.is_empty, context.getString(R.string.email)
                        )
                        !isValidEmail(email) -> context.getString(R.string.email_not_valid)
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

    fun isValidEmail(email: String): Boolean {
        val pattern = Regex(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        )
        return pattern.matches(email)
    }

    private val Number.dpToPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        )
}