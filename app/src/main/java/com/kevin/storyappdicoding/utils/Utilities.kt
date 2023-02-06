package com.kevin.storyappdicoding.utils

import android.content.res.Resources
import android.util.TypedValue

object Utilities {
    val Number.dpToPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
}