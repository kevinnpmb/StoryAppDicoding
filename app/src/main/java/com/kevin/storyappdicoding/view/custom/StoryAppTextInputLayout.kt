package com.kevin.storyappdicoding.view.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.utils.Utilities.dpToPx

class StoryAppTextInputLayout(context: Context, private val attr: AttributeSet) :
    TextInputLayout(context, attr) {
    private lateinit var title: String

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        init()
    }

    private fun init() {
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.StoryAppTextInputLayout)
        title = typedArray.getString(R.styleable.StoryAppTextInputLayout_title) ?: ""
        typedArray.recycle()
        boxBackgroundColor = ContextCompat.getColor(context, R.color.white)
        boxBackgroundMode = BOX_BACKGROUND_OUTLINE
        setBoxCornerRadii(8.dpToPx, 8.dpToPx, 8.dpToPx, 8.dpToPx)
        isExpandedHintEnabled = false
        hint = title
        editText?.setPaddingRelative(4, 0, 4, 0)
        editText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        editText?.hint = context.getString(R.string.enter_your, title)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = s.toString()
                error = if (text.isBlank()) {
                    context.getString(R.string.is_empty, title)
                } else {
                    null
                }

            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}