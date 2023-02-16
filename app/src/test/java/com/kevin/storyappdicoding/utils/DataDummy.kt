package com.kevin.storyappdicoding.utils

import com.kevin.storyappdicoding.data.model.Story
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object DataDummy {
    fun generateDummyStoryEntity(): List<Story> {
        val newsList = ArrayList<Story>()
        for (i in 0..10) {
            val news = Story(
                "story-$i",
                "Story $i",
                "Description $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                SimpleDateFormat("yyyy-MM-dd").format(Date()),
            )
            newsList.add(news)
        }
        return newsList
    }
}