package com.kevin.storyappdicoding.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_user")
@Parcelize
data class User(
    @PrimaryKey
    val email: String,
    val name: String,
    val password: String,
) : Parcelable
