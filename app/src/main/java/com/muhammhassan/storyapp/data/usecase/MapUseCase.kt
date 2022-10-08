package com.muhammhassan.storyapp.data.usecase

import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface MapUseCase {
    fun getStoryWithLocation(): Flow<ApiResponse<List<StoriesResponseModel>>>
}