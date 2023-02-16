package com.kevin.storyappdicoding.data.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float? = null,
    val lon: Float? = null,
) : Parcelable
