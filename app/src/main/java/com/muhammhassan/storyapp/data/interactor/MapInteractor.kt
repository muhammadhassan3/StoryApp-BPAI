package com.muhammhassan.storyapp.data.interactor

import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.repository.StoryRepository
import com.muhammhassan.storyapp.data.usecase.MapUseCase
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

class MapInteractor(private val repository: StoryRepository) : MapUseCase {
    override fun getStoryWithLocation(): Flow<ApiResponse<List<StoriesResponseModel>>> {
        return repository.getStoriesWithLocation()
    }

    companion object {
        private var INSTANCE: MapInteractor? = null

        fun getInstance(storyRepository: StoryRepository): MapInteractor =
            INSTANCE ?: synchronized(this) {
                val instance = MapInteractor(storyRepository)
                INSTANCE = instance
                instance
            }
    }
}