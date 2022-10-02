package com.muhammhassan.storyapp.data.interactor

import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.repository.StoryRepository
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

class StoryListInteractor(private val repository: StoryRepository): StoryListUseCase {
    override fun getPagingData(): Flow<PagingData<StoriesResponseModel>> = repository.getPagingStories()
    override fun getData(): Flow<ApiResponse<List<StoriesResponseModel>>> = repository.getStories()

    companion object{
        private var INSTANCE: StoryListInteractor? = null

        fun getInstance(repository: StoryRepository): StoryListInteractor =
            INSTANCE ?: synchronized(this){
                val instance = StoryListInteractor(repository)
                INSTANCE = instance
                instance
            }
    }
}