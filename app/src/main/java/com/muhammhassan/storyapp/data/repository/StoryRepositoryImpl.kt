package com.muhammhassan.storyapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.pagingsource.StoryPagingSource
import com.muhammhassan.storyapp.utils.Constant.PAGE_SIZE
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class StoryRepositoryImpl(private val remoteDataSource: RemoteDataSource): StoryRepository {
    override fun saveStory(
        image: File,
        desc: String
    ): Flow<ApiResponse<Any>> = flow {
        emit(ApiResponse.loading())
        val response = remoteDataSource.saveStory(image, desc)
        emit(response)
    }

    override fun getPagingStories(): Flow<PagingData<StoriesResponseModel>> {
        return Pager(config = PagingConfig(PAGE_SIZE)){
            StoryPagingSource(remoteDataSource)
        }.flow
    }

    override fun getStories(): Flow<ApiResponse<List<StoriesResponseModel>>> = flow{
        emit(ApiResponse.loading())
        val response = remoteDataSource.getAllStories(1,5)
        emit(response)
    }

    companion object{
        private var INSTANCE: StoryRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource) =
            INSTANCE ?: synchronized(this){
                val instance = StoryRepositoryImpl(remoteDataSource)
                INSTANCE = instance
                instance
            }
    }
}