package com.kevin.storyappdicoding.data.repository

import android.location.Location
import com.kevin.storyappdicoding.data.model.Response
import com.kevin.storyappdicoding.database.StoryDatabase
import com.kevin.storyappdicoding.database.model.Story
import com.kevin.storyappdicoding.utils.PreferencesHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val preferencesHelper: PreferencesHelper,
) {
    suspend fun storiesWithLocation(): Flow<Response<List<Story>>> {
        return flow {
            try {
                emit(Response.Loading)
                emit(Response.Success(storyDatabase.storyDao().getAllStoryWithLocation()))
            } catch (ex: Exception) {
                emit(Response.Error(ex.message.toString()))
            }
        }
    }

    suspend fun storyDetail(id: Int): Flow<Response<Story>> {
        return flow {
            try {
                emit(Response.Loading)
                emit(Response.Success(storyDatabase.storyDao().getStory(id)))
            } catch (ex: Exception) {
                emit(Response.Error(ex.message.toString()))
            }
        }
    }

    fun storiesForList(): Flow<Response<List<Story>>> {
        return flow {
            try {
                emit(Response.Loading)
                emit(Response.Success(storyDatabase.storyDao().getAllStory()))
            } catch (ex: Exception) {
                emit(Response.Error(ex.message.toString()))
            }
        }
    }

    suspend fun addStory(
        file: File,
        description: String,
        location: Location? = null
    ): Flow<Response<Long>> {
        return flow {
            try {
                emit(Response.Loading)
                emit(Response.Success(storyDatabase.storyDao().insertStory(Story(
                    id = 0,
                    name = preferencesHelper.user?.name ?: "Anonymous",
                    description = description,
                    photoPath = file.absolutePath,
                    lat = location?.latitude?.toFloat(),
                    lon = location?.longitude?.toFloat(),
                ))))
            } catch (ex: Exception) {
                emit(Response.Error(ex.message.toString()))
            }
        }

    }
}