package com.kevin.storyappdicoding.utils

import com.kevin.storyappdicoding.data.model.Story
import java.text.SimpleDateFormat
import java.util.*

object DataDummy {
    fun generateDummyStoryEntity(): List<Story> {
        val storyList = mutableListOf<Story>()
        for (i in 0..100) {
            val news = Story(
                "story-$i",
                "Story $i",
                "Description $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                SimpleDateFormat("yyyy-MM-dd").format(Date()),
            )
            storyList.add(news)
        }
        return storyList
    }
}