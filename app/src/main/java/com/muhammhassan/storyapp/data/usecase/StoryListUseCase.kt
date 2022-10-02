package com.muhammhassan.storyapp.data.usecase

import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface StoryListUseCase {
    fun getPagingData(): Flow<PagingData<StoriesResponseModel>>
    fun getData(): Flow<ApiResponse<List<StoriesResponseModel>>>
}