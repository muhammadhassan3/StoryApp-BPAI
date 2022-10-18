package com.muhammhassan.storyapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.pagingsource.StoryPagingSource
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class FakeStoryRepository(private val dataSource: RemoteDataSource) : StoryRepository {
    override fun saveStory(
        image: File,
        desc: String,
        lat: Double?,
        lon: Double?
    ): Flow<ApiResponse<Any>> = flow {
        emit(dataSource.saveStory(image, desc, lat, lon))
    }

    override fun getPagingStories(): Flow<PagingData<StoriesResponseModel>> =
        Pager(config = PagingConfig(1)) {
            StoryPagingSource(dataSource)
        }.flow

    override fun getStoriesWithLocation(): Flow<ApiResponse<List<StoriesResponseModel>>> = flow {
        emit(dataSource.getStoriesWithLocation())
    }
}