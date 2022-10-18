package com.muhammhassan.storyapp.data.repository

import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryRepository {
    fun saveStory(image: File, desc: String, lat: Double?, lon: Double?): Flow<ApiResponse<Any>>
    fun getPagingStories(): Flow<PagingData<StoriesResponseModel>>
    fun getStoriesWithLocation(): Flow<ApiResponse<List<StoriesResponseModel>>>
}