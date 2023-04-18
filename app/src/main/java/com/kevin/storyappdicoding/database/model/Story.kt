package com.kevin.storyappdicoding.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_story")
@Parcelize
data class Story(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val photoPath: String,
    val lat: Float? = null,
    val lon: Float? = null,
) : Parcelable
