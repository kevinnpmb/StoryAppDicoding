package com.kevin.storyappdicoding.data.repository

import com.kevin.storyappdicoding.data.model.Response
import com.kevin.storyappdicoding.database.StoryDatabase
import com.kevin.storyappdicoding.database.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val storyDatabase: StoryDatabase) {
    suspend fun loginUser(email: String, password: String): Flow<Response<Pair<Boolean, User?>>> {
        return flow {
            try {
                emit(Response.Loading)
                emit(
                    Response.Success(
                        if (storyDatabase.userDao().isLoginDataTrue(email, password)) {
                            Pair(true, storyDatabase.userDao().getUser(email))
                        } else {
                            Pair(false, null)
                        }
                    )
                )
            } catch (ex: Exception) {
                emit(Response.Error(ex.message.toString()))
            }
        }
    }

    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Flow<Response<Long>> {
        return flow {
            try {
                emit(Response.Loading)
                emit(
                    Response.Success(
                        storyDatabase.userDao().insertUser(
                            User(
                                email, name, password
                            )
                        )
                    )
                )
            } catch (ex: Exception) {
                emit(Response.Error(ex.message.toString()))
            }
        }
    }
}