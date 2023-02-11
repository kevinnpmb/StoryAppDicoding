package com.kevin.storyappdicoding.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String,
    val name: String,
    val token: String,
    val email: String? = null,
) : Parcelable
