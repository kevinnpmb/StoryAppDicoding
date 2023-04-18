package com.kevin.storyappdicoding.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevin.storyappdicoding.database.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM table_user WHERE email = :email")
    fun getUser(email: String): User

    @Query("SELECT EXISTS(SELECT 1 FROM table_user WHERE name = :name)")
    suspend fun isUserExist(name: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM table_user WHERE email = :email AND password = :password)")
    suspend fun isLoginDataTrue(email: String, password: String): Boolean

    @Query("DELETE FROM table_user")
    suspend fun deleteAll()
}